package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Color;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.lib.SoundManager;
import com.blogspot.sontx.dut.game.mgr.helper.BrickGenerator;
import com.blogspot.sontx.dut.game.mgr.helper.GameInfoPanel;
import com.blogspot.sontx.dut.game.obj.Ball;
import com.blogspot.sontx.dut.game.obj.Bar;
import com.blogspot.sontx.dut.game.obj.Border;
import com.blogspot.sontx.dut.game.obj.Brick;
import com.blogspot.sontx.dut.game.obj.GameObject;
import com.blogspot.sontx.dut.game.obj.Hole;

import java.util.List;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 17/1/2016.
 */
public abstract class LevelManager extends SceneManager {
    public static final int GAME_PLAYING                        = 1;
    public static final int GAME_WIN                            = 2;
    public static final int GAME_OVER                           = 4;
    public static final int GAME_PAUSED                         = 8;

    protected static final float HOLE_RADIUS                    = 25.0f;
    protected static final float HOLE_HEIGHT                    = 50.0f;

    protected static final float BALL_RADIUS                    = 20.0f;

    private static final float BAR_WIDTH                        = 250.0f;
    private static final float BAR_HEIGHT                       = 20.0f;
    private static final float BAR_MARGIN_BOTTOM                = 300.0f;

    private static final int SOUND_INDEX_COLLISION_WALL         = 0;
    private static final int SOUND_INDEX_COLLISION_BRICK        = 1;
    private static final int SOUND_INDEX_COLLISION_BAR          = 2;
    private static final int SOUND_INDEX_WIN                    = 3;

    private static final float GAME_INFO_PANEL_HEIGHT           = 75.0f;
    private static final float GAME_INFO_PANEL_MARGIN_TOP       = 50.0f;
    private static final float GAME_INFO_PANEL_MARGIN_LEFT      = 10.0f;
    private static final float GAME_INFO_PANEL_MARGIN_RIGHT     = GAME_INFO_PANEL_MARGIN_LEFT;
    private static final float GAME_INFO_PANEL_MARGIN_BOTTOM    = GAME_INFO_PANEL_MARGIN_TOP;

    protected final RectF mPlayableBound;

    private Ball mBall;
    private Hole mHole;
    private Bar mBar;
    private Border mBorder;

    private BrickGenerator mBrickGenerator;
    private GameInfoPanel mGameInfoPanel;

    private int mGameState = GAME_PLAYING;
    private boolean mLastCollisionWithBar = false;
    private boolean mLastCollisionWithWall = false;

    public LevelManager() {
        float left = clientRectangle.left;
        float top = clientRectangle.top + GAME_INFO_PANEL_HEIGHT +
                GAME_INFO_PANEL_MARGIN_TOP + GAME_INFO_PANEL_MARGIN_BOTTOM;
        float right = clientRectangle.right;
        float bottom = clientRectangle.bottom;
        mPlayableBound = new RectF(left, top, right, bottom);
    }

    public int getGameState() {
        return mGameState;
    }

    public abstract int getCurrentLevel();

    protected abstract float getBallSpeed();

    protected abstract int getCountdownMinutes();

    protected abstract int getCountdownSeconds();

    protected int getBrickColumns() {
        return 8;
    }

    private void generateBall() {
        Ball ball = new Ball(
                mBar.getLeft() + mBar.getWidth() / 2.0f - BALL_RADIUS,
                mBar.getTop() - BALL_RADIUS * 4.0f,
                BALL_RADIUS);
        ball.setSpeedY(-getBallSpeed());
        ball.setSpeedX(-getBallSpeed());
        this.mBall = ball;
        registerObject(ball);
    }

    protected void setHole(Hole hole) {
        this.mHole = hole;
        registerObject(hole);
    }

    protected void generateBricks(int countOfBrick, int level) {
        for (int i = 0; i < countOfBrick; i++) {
            Brick brick = mBrickGenerator.generateBrick(level);
            if (brick == null)
                break;
        }
    }

    private void reflect(int direction) {
        switch (direction) {
            case Ball.VERTICAL_COLLISION:
                mBall.setSpeedX(-mBall.getSpeedX());
                break;
            case Ball.HORIZONTAL_COLLISION:
                mBall.setSpeedY(-mBall.getSpeedY());
                break;
        }
    }

    private void doCollisionWithBrick(Brick brick, int direction) {
        SoundManager.playSound(SOUND_INDEX_COLLISION_BRICK);
        mBrickGenerator.releaseBrick(brick);
        mGameInfoPanel.addScore(brick.getLevel() + 1);
        reflect(direction);
    }

    private void doCollisionWithBorder(int direction) {
        SoundManager.playSound(SOUND_INDEX_COLLISION_WALL);
        reflect(direction);
    }

    private void doCollisionWithBar(int direction) {
        SoundManager.playSound(SOUND_INDEX_COLLISION_BAR);
        reflect(direction);
    }

    private void doCollisionWithHole() {
        mGameState |= GAME_PAUSED;
        mGameState |= GAME_WIN;
        SoundManager.playSound(SOUND_INDEX_WIN);
    }

    private void initializeSounds() {
        SoundManager.reset();
        SoundManager.addSound(SOUND_INDEX_COLLISION_WALL, R.raw.collision_wall);
        SoundManager.addSound(SOUND_INDEX_COLLISION_BRICK, R.raw.collision_brick);
        SoundManager.addSound(SOUND_INDEX_COLLISION_BAR, R.raw.collision_bar);
        SoundManager.addSound(SOUND_INDEX_WIN, R.raw.win);
    }

    private void initializeBorder() {
        mBorder = new Border(mPlayableBound, 1.0f, Color.GRAY);
        registerObject(mBorder);
    }

    private void initializeBrickGenerator() {
        RectF brickArea = new RectF(mPlayableBound);
        brickArea.bottom = mPlayableBound.top + mPlayableBound.height() * 0.5f;
        brickArea.top += HOLE_RADIUS * 2.0f;
        mBrickGenerator = new BrickGenerator(getBrickColumns(), brickArea, this);
    }

    private void initializeGameInfoPanel() {
        RectF panelArea = new RectF(
                clientRectangle.left + GAME_INFO_PANEL_MARGIN_LEFT,
                clientRectangle.top + GAME_INFO_PANEL_MARGIN_TOP,
                clientRectangle.right - GAME_INFO_PANEL_MARGIN_RIGHT,
                clientRectangle.top + GAME_INFO_PANEL_HEIGHT + GAME_INFO_PANEL_MARGIN_TOP);
        mGameInfoPanel = new GameInfoPanel(panelArea, getCountdownMinutes(), getCountdownSeconds(), this);
        mGameInfoPanel.setLevel(getCurrentLevel());
    }

    private void initializeBar() {
        mBar = new Bar(mPlayableBound.left + (mPlayableBound.width() - BAR_WIDTH) / 2.0f,
                mPlayableBound.bottom - BAR_MARGIN_BOTTOM - BAR_HEIGHT);
        mBar.setHeight(BAR_HEIGHT);
        mBar.setWidth(BAR_WIDTH);
        mBar.setMovableBound(new RectF(mPlayableBound.left, 0.0f, mPlayableBound.right, 0.0f));
        mBar.setExtendWidth(100f);
        registerObject(mBar);
    }

    @Override
    public void init() {
        initializeSounds();
        initializeBorder();
        initializeBrickGenerator();
        initializeGameInfoPanel();
        initializeBar();
        generateBall();
    }

    @Override
    public void destroy() {
        super.destroy();
        SoundManager.reset();
    }

    private boolean checkBarCollision() {
        if (!mLastCollisionWithBar && mBall.isCollision(mBar)) {
            doCollisionWithBar(mBall.checkOuterCollision(mBar));
            mLastCollisionWithBar = true;
            return true;
        }
        mLastCollisionWithBar = false;
        return false;
    }

    private boolean checkBricksCollision() {
        Iterable<GameObject> objects = getObjects();
        for (GameObject object : objects) {
            if (object instanceof Brick) {
                if (mBall.isCollision(object)) {
                    doCollisionWithBrick((Brick) object, mBall.checkOuterCollision(object));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkWallCollision() {
        if (!mLastCollisionWithWall && mBall.isCollision(mBorder)) {
            mLastCollisionWithWall = true;
            if (mBall.isInnerHorizontalCollision(mBorder)) {
                doCollisionWithBorder(Ball.HORIZONTAL_COLLISION);
                return true;
            }
            if (mBall.isInnerVerticalCollision(mBorder)) {
                doCollisionWithBorder(Ball.VERTICAL_COLLISION);
                return true;
            }
        }
        mLastCollisionWithWall = false;
        return false;
    }

    private boolean checkHoleCollision() {
        if (mBall.isCollision(mHole)) {
            doCollisionWithHole();
            return true;
        }
        return false;
    }

    private void updateGameInfo() {
        mGameInfoPanel.update();
        if (mGameInfoPanel.isTimeout()) {
            mGameState |= GAME_OVER;
        }
    }

    @Override
    public void update() {
        if ((mGameState & GAME_PAUSED) == GAME_PAUSED)
            return;
        super.update();

        updateGameInfo();

        if (checkBarCollision())
            return;
        if (checkBricksCollision())
            return;
        if (checkWallCollision())
            return;
        checkHoleCollision();
    }
}
