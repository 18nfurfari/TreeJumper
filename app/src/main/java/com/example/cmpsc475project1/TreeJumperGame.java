package com.example.cmpsc475project1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.telephony.BarringInfo;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Rect;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class TreeJumperGame extends View implements Runnable {

    private Paint paint;
    private float boxHeight = 100;
    private boolean isJumping;

    private float characterX, characterY;
    private float treeX, treeY;
    private float characterSpeedY;
    private float treeSpeedX;
    private long lastUpdateTime;

    private Bitmap characterBitmap;
    private Bitmap treeBitmap;
    public int score;
    private boolean playerAlive;
    private ScoreDatabase db;
    private MainActivity mainActivity;

    private Thread gameThread;
    private volatile boolean running = false;

    private void init() {
        paint = new Paint();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap characterBitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.ninja_final, options);
        characterBitmap = Bitmap.createScaledBitmap(characterBitmapTemp, 200, 200, true);

        Bitmap treeBitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.tree, options);
        treeBitmap = Bitmap.createScaledBitmap(treeBitmapTemp, 200, 400, true);

        characterX = 50;
        characterY = getHeight() - characterBitmap.getHeight();
        treeX = getWidth();
        treeY = getHeight() - treeBitmap.getHeight();

        characterSpeedY = 0;
        treeSpeedX = 900;
        lastUpdateTime = System.currentTimeMillis();
        score = 0;
        playerAlive = true;

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    public TreeJumperGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
        init();
    }


    public TreeJumperGame(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        characterY = h - boxHeight;
        characterX = 0;
        treeX = w - treeBitmap.getWidth();
        treeY = h - treeBitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(characterBitmap, characterX, characterY, paint);
        canvas.drawBitmap(treeBitmap, treeX, treeY, paint);

        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setTextSize(200);
        canvas.drawText("Score: " + score, 10, 200, paint);

        // update();
    }

    private void update() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;

        // Update character position based on jumping
        characterY += characterSpeedY * deltaTime;

        // Update tree position (move from right to left)
        treeX -= treeSpeedX * deltaTime;

        // Check if the tree reached the left side of the screen
        if (treeX + treeBitmap.getWidth() < 0 && playerAlive) {
            treeX = getWidth();
            score++;
        }

        checkLanding();
        invalidate();
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            characterSpeedY = -6000;
        }
    }

    private void checkLanding() {
        float groundY = getHeight() - characterBitmap.getHeight();
        if (characterY >= groundY) {
            characterY = groundY;
            characterSpeedY = 0;
            // Have to add this for multiple jumps
            isJumping = false;

        } else {
            characterSpeedY += 250;
        }

        if (playerAlive && checkCollision()) {
            playerAlive = false;
            post(new Runnable() {
                @Override
                public void run() {
                    showGameOverDialog();
                }
            });
        }
    }

    private boolean checkCollision() {
        Rect characterRect = new Rect((int) characterX, (int) characterY, (int) (characterX + characterBitmap.getWidth()), (int) (characterY + characterBitmap.getHeight()));
        Rect treeRect = new Rect((int) treeX, (int) treeY, (int) (treeX + treeBitmap.getWidth()), (int) (treeY + treeBitmap.getHeight()));

        return characterRect.intersect(treeRect);
    }

    private void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Game Over")
                .setMessage("Your score: " + score)
                .setCancelable(false)
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        restartGame();
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Save the score
                        if (mainActivity != null) {
                            mainActivity.onGameOver(score);
                            mainActivity.mainMenuOnClick(null);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void restartGame() {
        characterX = 50;
        characterY = getHeight() - characterBitmap.getHeight();
        treeX = getWidth();
        treeY = getHeight() - treeBitmap.getHeight();
        characterSpeedY = 0;
        treeSpeedX = 900;
        lastUpdateTime = System.currentTimeMillis();
        score = 0;
        isJumping = false;
        playerAlive = true;
    }

    @Override
    public void run() {
        while (running) {
            update();
            postInvalidate();

            try {
                Thread.sleep(16); // Limit the frame rate to ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
