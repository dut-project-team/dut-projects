package com.blogspot.sontx.dut.game.mgr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

import com.blogspot.sontx.dut.game.App;
import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.lib.SoundManager;
import com.blogspot.sontx.dut.game.lib.SystemAlert;
import com.blogspot.sontx.dut.game.lib.Timer;
import com.blogspot.sontx.dut.game.mgr.helper.BricksHolder;
import com.blogspot.sontx.dut.game.obj.Ball;
import com.blogspot.sontx.dut.game.obj.Bar;
import com.blogspot.sontx.dut.game.obj.Border;
import com.blogspot.sontx.dut.game.obj.Brick;
import com.blogspot.sontx.dut.game.obj.Hole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noem on 17/1/2016.
 */
public abstract class PlayingManager extends SceneManager {
    protected static final float HOLE_RADIUS = 50.0f;
    protected static final float HOLE_HEIGHT = 20.0f;
    protected static final float BALL_RADIUS = 20.0f;
    private static final float BALL_SPEED = 7.0f;
    private static final long BRICK_AUTO_GENERATE_AFTER = 5000;
    private static final float BRICK_PROBABILITY_LEVEL0 = 15.0f;
    private static final float BRICK_PROBABILITY_LEVEL1 = 25.0f;
    private static final float BRICK_PROBABILITY_LEVEL2 = 60.0f;
    private static final float BAR_WIDTH = 250.0f;
    private static final float BAR_HEIGHT = 20.0f;
    private static final float BAR_MARGIN_BOTTOM = 300.0f;
    private static final int SOUND_COLLISION_WALL = 0;
    private static final int SOUND_COLLISION_BRICK = 1;
    private static final int SOUND_COLLISION_BAR = 2;
    private static final int SOUND_WIN = 3;

    public static final int GAME_PLAYING = 1;
    public static final int GAME_WIN = 2;
    public static final int GAME_OVER = 4;
    public static final int GAME_PAUSED = 8;

    protected final Bar mBar;
    protected final List<Ball> mBalls = new ArrayList<>();
    protected final List<Hole> mHoles = new ArrayList<>();
    private final Border mBorder;
    protected BricksHolder mBricksHolder;
    private int mGameState = GAME_PLAYING;

    public PlayingManager() {
        mBorder = new Border(new RectF(DRAWABLE_X, DRAWABLE_Y, DRAWABLE_X + DRAWABLE_WIDTH, DRAWABLE_Y + DRAWABLE_HEIGHT), 3.0f, Color.GRAY);
        mObjects.add(mBorder);

        mBar = new Bar(DRAWABLE_X + (DRAWABLE_WIDTH - BAR_WIDTH) / 2.0f, DRAWABLE_Y + DRAWABLE_HEIGHT - BAR_MARGIN_BOTTOM - BAR_HEIGHT, Color.RED);
        mBar.setHeight(BAR_HEIGHT);
        mBar.setWidth(BAR_WIDTH);
        mBar.setMovableBound(new RectF(DRAWABLE_X, 0.0f, DRAWABLE_X + DRAWABLE_WIDTH, 0.0f));
        mBar.setExtendWidth(100f);
        mObjects.add(mBar);

        mBricksHolder = new BricksHolder(4, new RectF(DRAWABLE_X, DRAWABLE_Y, DRAWABLE_X + DRAWABLE_WIDTH, (DRAWABLE_Y + DRAWABLE_HEIGHT) * 0.5f));
    }

    public int getGameState() {
        return mGameState;
    }

    public abstract int getCurrentLevel();

    protected void generateBall() {
        Ball ball = new Ball(
                mBar.getLeft() + mBar.getWidth() / 2.0f - BALL_RADIUS,
                mBar.getTop() - BALL_RADIUS * 2.0f,
                BALL_RADIUS,
                Color.MAGENTA);
        ball.setSpeedY(-BALL_SPEED);
        ball.setSpeedX(-BALL_SPEED);
        addBall(ball);
    }

    protected void addBall(Ball ball) {
        mBalls.add(ball);
        mObjects.add(ball);
    }

    protected void addHole(Hole hole) {
        mHoles.add(hole);
        mObjects.add(hole);
    }

    protected void generateBricks(int max) {
        for (int i = 0; i < max; i++) {
            int level;
            double rand = Math.random() * 100.0f;
            if (rand <= BRICK_PROBABILITY_LEVEL0)
                level = Brick.LEVEL0;
            else if (rand <= BRICK_PROBABILITY_LEVEL1)
                level = Brick.LEVEL1;
            else
                level = Brick.LEVEL2;
            Brick brick = mBricksHolder.generateBrick(level);
            mObjects.add(brick);
        }
    }

    private void reflect(Ball ball, int dir) {
        switch (dir) {
            case Ball.VERTICAL_COLLISION:
                ball.setSpeedX(-ball.getSpeedX());
                break;
            case Ball.HORIZONTAL_COLLISION:
                ball.setSpeedY(-ball.getSpeedY());
                break;
            default:
                //ball.setSpeedX(-ball.getSpeedX());
                //ball.setSpeedY(-ball.getSpeedY());
        }
    }

    private void doCollisionWithBrick(Brick brick, Ball ball, int dir) {
        SoundManager.playSound(SOUND_COLLISION_BRICK);
        reflect(ball, dir);
        Timer.postDelay(new Runnable() {
            @Override
            public void run() {
                generateBricks(1);
            }
        }, BRICK_AUTO_GENERATE_AFTER);
        mBricksHolder.removeBrick(brick);
        mObjects.remove(brick);
    }

    private void doCollisionWithBorder(Ball ball, int dir) {
        SoundManager.playSound(SOUND_COLLISION_WALL);
        reflect(ball, dir);
    }

    private void doCollisionWithBar(Ball ball, int dir) {
        SoundManager.playSound(SOUND_COLLISION_BAR);
        //ball.setSpeedX(ball.getSpeedX() + mBar.getSpeedX());
        reflect(ball, dir);
    }

    private void doCollisionWithHole(Ball ball, Hole hole) {
        mGameState |= GAME_PAUSED;
        mGameState |= GAME_WIN;
        SoundManager.playSound(SOUND_WIN);
    }

    @Override
    public void init() {
        SoundManager.reset();
        SoundManager.addSound(SOUND_COLLISION_WALL, R.raw.collision_wall);
        SoundManager.addSound(SOUND_COLLISION_BRICK, R.raw.collision_brick);
        SoundManager.addSound(SOUND_COLLISION_BAR, R.raw.collision_bar);
        SoundManager.addSound(SOUND_WIN, R.raw.win);
    }

    @Override
    public void update() {
        if ((mGameState & GAME_PAUSED) == GAME_PAUSED)
            return;
        super.update();
        Ball ball = mBalls.get(0);

        if (ball.isCollision(mBar)) {
            doCollisionWithBar(ball, ball.checkOuterCollision(mBar));
        }

        List<Brick> bricks = mBricksHolder.getBricks();
        for (Brick obj : bricks) {
            if (ball.isCollision(obj)) {
                doCollisionWithBrick(obj, ball, ball.checkOuterCollision(obj));
                return;
            }
        }

        for (Hole hole : mHoles) {
            if (ball.isCollision(hole)) {
                doCollisionWithHole(ball, hole);
                return;
            }
        }

        if (ball.isInnerHorizontalCollision(mBorder))
            doCollisionWithBorder(ball, Ball.HORIZONTAL_COLLISION);
        if (ball.isInnerVerticalCollision(mBorder))
            doCollisionWithBorder(ball, Ball.VERTICAL_COLLISION);
    }
}
