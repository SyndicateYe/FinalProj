package com.ece420.lab7;

import static com.ece420.lab7.CameraActivity.originalImage1;
import static com.ece420.lab7.CameraActivity.originalImage2;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.core.Mat;

import java.util.ArrayList;


public class PossionEdit extends AppCompatActivity {    //implements SurfaceHolder.Callback
    Bitmap m_targetImage;
    Bitmap m_cutImage;
    ArrayList<Point2> m_cutPoints;
    private ArrayList<Point2> m_borderPoints;
    int[][] m_mask = {{1,0},{0,1}};
    private int m_imageX = 0;
    private int m_imageY = 0;
    private int m_imageW = 0;
    private int m_imageH = 0;
    private int m_maskW = 0;
    private int m_maskH = 0;

    private static final int MASK_INSIDE = -1;
    private static final int MASK_BORDER = -2;
    private static final int MASK_OUTSIDE = -3;
    boolean m_useMixedGradients;

    ImageView fImage;
    ImageView oImage1;
    ImageView oImage2;

    private SeekBar horizontalBar;
    private SeekBar verticalBar;
    private SeekBar sizeBar;

    private TextView horizontalText;
    private TextView verticalText;
    private TextView sizeText;

    private double horizontal_ = 0.5;
    private double vertical_ = 0.5;
    private double size_ = 0.5;

    Bitmap finalImage;
    Button mask_Button;
    private int ss_flag = -1;

//    PossionEqSolver Solver = new PossionEqSolver(m_targetImage, m_cutImage, m_cutPoints,
//                                      m_mask, m_imageX, m_imageY, m_useMixedGradients);


//    PossionEqSolver Solver = new PossionEqSolver(originalImage1, originalImage2, m_cutPoints,
//            m_mask, m_imageX, m_imageY, false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_poisson);
        super.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        clearMask();
//        getCutAreaPoints();
//        getCutImage();
//        Solver solver = new Solver(m_targetImage, m_cutImage, m_cutPoints,
//                m_mask, m_imageX, m_imageY, m_useMixedGradients);
//        solver.run();
//        solver.updateTarget();
//        Solver.run();
//        Solver.updateTarget();
        //finalImage = Solver.updateTarget();

        // Setup horizontal seek bar
        horizontalBar = (SeekBar) findViewById(R.id.hSeekBar);
        horizontalBar.setMax(100);
        horizontalBar.setProgress(50);
        horizontalBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                horizontal_ =  (double)horizontalBar.getProgress() / 100;
                poisson_edit_python(originalImage1, originalImage2, null, new int[]{82, 95}, 0);
                Toast.makeText(PossionEdit.this,
                        "The horizontal position is: " + horizontal_,
                        Toast.LENGTH_SHORT).show();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Setup vertical seek bar
        verticalBar = (SeekBar) findViewById(R.id.vSeekBar);
        verticalBar.setMax(100);
        verticalBar.setProgress(50);
        verticalBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                // Only allow modification when not tracking
                vertical_ = (double)verticalBar.getProgress() / 100;
                poisson_edit_python(originalImage1, originalImage2, null, new int[]{82, 95}, 0);
                Toast.makeText(PossionEdit.this,
                        "The vertical position is: " + vertical_,
                        Toast.LENGTH_SHORT).show();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Setup width seek bar
        sizeBar = (SeekBar) findViewById(R.id.sSeekBar);
        sizeBar.setMax(100);
        sizeBar.setProgress(50);
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                // Only allow modification when not tracking
                size_ = (double)sizeBar.getProgress() / 100;
                poisson_edit_python(originalImage1, originalImage2, null, new int[]{82, 95}, 0);
                Toast.makeText(PossionEdit.this,
                        "The size is: " + size_,
                        Toast.LENGTH_SHORT).show();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // VIEWS
        fImage = (ImageView) findViewById(R.id.fi_im);
        oImage1 = (ImageView) findViewById(R.id.ma_im);
        oImage2 = (ImageView) findViewById(R.id.ta_im);

        oImage1.setImageBitmap(originalImage1);
        oImage2.setImageBitmap(originalImage2);

        Bitmap mask_bitmap;

        // https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitma
//        BitmapDrawable mask_ = (BitmapDrawable) oImage2.getDrawable();
//        mask_bitmap = mask_.getBitmap();
//
//
//        BitmapDrawable final_image = (BitmapDrawable) fImage.getDrawable();
//        Bitmap final_imageBitmap = final_image.getBitmap();

//        oImage1.buildDrawingCache();
//        Bitmap source_bitmap = oImage1.getDrawingCache();


//        oImage2.buildDrawingCache();
//        Bitmap target_bitmap = oImage2.getDrawingCache();
//
//        fImage.buildDrawingCache();
//        Bitmap final_imageBitmap = fImage.getDrawingCache();

        Mat targ;

        poisson_edit_python(originalImage1, originalImage2, null, new int[]{82, 95}, 0);


        horizontalText = (TextView) findViewById(R.id.HorizontalTextView);
        verticalText = (TextView) findViewById(R.id.verticalTextView);
        sizeText = (TextView) findViewById(R.id.size);
        mask_Button = (Button)findViewById((R.id.PoinssonButton));
        mask_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ss_flag == -1) {
                    // Modify UI
                    mask_Button.setText("STOP");
                    horizontalText.setVisibility(View.INVISIBLE);
                    horizontalBar.setVisibility(View.INVISIBLE);
                    verticalText.setVisibility(View.INVISIBLE);
                    verticalBar.setVisibility(View.INVISIBLE);
                    sizeText.setVisibility(View.INVISIBLE);
                    sizeBar.setVisibility(View.INVISIBLE);
                    // Modify tracking flag
                    ss_flag = 1;
                    poisson_edit_python(originalImage1, originalImage2, null, new int[]{82, 95}, 1);
                }
                else if(ss_flag == 1){
                    // Modify UI
                    mask_Button.setText("START");
                    horizontalText.setVisibility(View.VISIBLE);
                    horizontalBar.setVisibility(View.VISIBLE);
                    verticalText.setVisibility(View.VISIBLE);
                    verticalBar.setVisibility(View.VISIBLE);
                    sizeText.setVisibility(View.VISIBLE);
                    sizeBar.setVisibility(View.VISIBLE);
                    // Tear down myTracker
//                    myTacker.clear();
                    // Modify tracking flag
                    ss_flag = -1;
                }
            }
        });



//        Imgproc.cvtColor(targ, targ, Imgproc.COLOR_BGR2RGB);
//        Bitmap img = null;
//        try {
//            img = Bitmap.createBitmap(targ.cols(), targ.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(targ, img);
//        }
//        catch (CvException e){
//            Log.d("Exception",e.getMessage());
//        }
//
//        fImage.setImageBitmap(img);
//        fImage.setImageBitmap(finalImage);
    }
//
//    private void clearMask()
//    {
//        for (int x = 0; x < m_maskW; x++)
//        {
//            for (int y = 0; y < m_maskH; y++)
//            {
//                m_mask[x][y] = MASK_INSIDE;
//            }
//        }
//    }
//
//    private void getCutAreaPoints()
//    {
//        // Fill in pixels of border
//        int len = m_borderPoints.size();
//        for (int i = 0; i < len; i++)
//        {
//            Point2 start = m_borderPoints.get(i);
//            start = clipPointToBox(0, 0, m_maskW - 1, m_maskH - 1, start.x, start.y);
//            Point2 end = m_borderPoints.get((i + 1) % len);
//            end = clipPointToBox(0, 0, m_maskW - 1, m_maskH - 1, end.x, end.y);
//            Point2 d = end.sub(start);
//
//            double r = 1.0 / (double) Math.abs(d.x);
//            int dir = d.x > 0 ? 1 : -1;
//            for (int x = 0; x < Math.abs(d.x); x++)
//            {
//
//                int h = (int) Math.round(r * (double)x * (double) d.y);
//                m_mask[start.x + x * dir][start.y + h] = MASK_BORDER;
//            }
//
//            r = 1.0 / (double) Math.abs(d.y);
//            dir = d.y > 0 ? 1 : -1;
//
//            for (int y = 0; y < Math.abs(d.y); y++)
//            {
//                int w = (int) Math.round(r * (double)y * (double) d.x);
//                m_mask[start.x + w][start.y + y * dir] = MASK_BORDER;
//            }
//        }
//
//        // Find an outside point
//        Stack<Point2> stack = new Stack<>();
//        outer:
//        for (int y = 0; y < m_maskH; y++)
//        {
//            for (int x = 0; x < m_maskW; x++)
//            {
//                if (m_mask[x][y] == MASK_INSIDE)
//                {
//                    stack.push(new Point2(x, y));
//                    break outer;
//                }
//            }
//        }
//
//        // Fill all outside points
//        while (!stack.empty())
//        {
//            Point2 p = stack.pop();
//            if (p.x < 0 || p.x > m_maskW - 1 || p.y < 0 || p.y > m_maskH - 1)
//            {
//                continue;
//            }
//            if (m_mask[p.x][p.y] == MASK_OUTSIDE)
//            {
//                continue;
//            }
//            if (m_mask[p.x][p.y] == MASK_BORDER)
//            {
//                continue;
//            }
//            m_mask[p.x][p.y] = MASK_OUTSIDE;
//            stack.push(new Point2(p.x + 1, p.y));
//            stack.push(new Point2(p.x - 1, p.y));
//            stack.push(new Point2(p.x, p.y + 1));
//            stack.push(new Point2(p.x, p.y - 1));
//        }
//
//        m_cutPoints.clear();
//        for (int y = 0; y < m_maskH; y++)
//        {
//            for (int x = 0; x < m_maskW; x++)
//            {
//                if (m_mask[x][y] == MASK_INSIDE)
//                {
//                    m_cutPoints.add(new Point2(x - m_imageX, y - m_imageY));
//                    m_mask[x][y] = m_cutPoints.size() - 1;
//                }
//            }
//        }
//    }
//
//    private void getCutImage()
//    {
//        m_cutImage = new Bitmap(m_imageW, m_imageH, Bitmap.TYPE_INT_ARGB); //Bitmap.TYPE_INT_ARGB problem
//        for (int y = 0; y < m_imageH; y++)
//        {
//            for (int x = 0; x < m_imageW; x++)
//            {
//                int rx = m_imageX + x;
//                int ry = m_imageY + y;
//
//                if (!(rx < 0 || rx >= m_maskW || ry < 0 || ry >= m_maskH)
//                        && m_mask[rx][ry] > MASK_INSIDE)
//                {
//                    int c = m_sourceImage.getRGB(x, y);     //need change to color like in solver
//                    Color newC = new Color((c & 0x00ff0000) >> 16, (c & 0x0000ff00) >> 8, c & 0x000000ff, 255);
//                    m_cutImage.setRGB(x, y, newC.getRGB());
//
//                }
//                else
//                {
//                    Color newC = new Color(255, 255, 255, 0);
//                    m_cutImage.setRGB(x, y, newC.getRGB());
//                }
//            }
//        }
//    }
//
//    private Point2 clipPointToBox(int x0, int y0, int w, int h, int x, int y)
//    {
//        int px = x;
//        int py = y;
//
//        px = Math.max(x0, px);
//        px = Math.min(x0 + w, px);
//        py = Math.max(y0, py);
//        py = Math.min(y0 + h, py);
//
//        return new Point2(px, py);
//    }

    public static int[][] laplacian_matrix(int n, int m){
        int[][] mat_D = new int[m][m];
        for(int i = 0; i < m - 1; ++i){
            mat_D[i][i+1] = -1;
        }
        for(int i = 0; i < m-1; ++i){
            mat_D[i+1][i] = -1;
        }
        for(int i = 0; i < m; ++i){
            mat_D[i][i] = 4;
        }
        int dim = n*m;
        int[][] mat_A = new int[dim][dim];
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; j++){
                for(int k = 0; k < m; k++){
                    mat_A[n*4+j][n*4+k] = mat_D[j][k];
                }
            }
        }
        for(int i = 0; i < dim - m; i++){
            mat_A[i][i+m] = -1;
        }
        for(int i = 0; i < dim - m; i++) {
            mat_A[i + m][i] = -1;
        }
        return mat_A;
    }


//    public static int[][][] _tocsc_(int n, int m){
//        ret
//    }


// public Mat poisson_edit_python(Bitmap source, Bitmap target, Bitmap mask, int[] offset)
    public void poisson_edit_python(Bitmap source, Bitmap target, Bitmap mask, int[] offset, int needed) {
        // target.getWidth();
        float y_max = target.getHeight();
        float x_max = target.getWidth();
        float y_min = 0;
        float x_min = 0;

        float x_range = x_max - x_min;
        float y_range = y_max - y_min;

        // draw the source inside of the empty bitmap
        int startX= (target.getHeight() - source.getHeight()) / 2; //for horisontal position
        int startY= (target.getWidth() - source.getWidth()) / 2;//for vertical position
        // canvas.drawBitmap(gball, startX, startY, null);

        // create an empty bitmap
        // https://stackoverflow.com/questions/5663671/creating-an-empty-bitmap-and-drawing-though-canvas-in-android

        // Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        // mask_bitmap = Bitmap.createBitmap(w, h, target_bitmap.getConfig()); // this creates a MUTABLE bitmap

        if(size_ <= 0.1){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.1), (int) (target.getHeight() * 0.1), true);
        }
        else if (size_ <= 0.2){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.2), (int) (target.getHeight() * 0.2), true);
        }
        else if(size_ <= 0.3){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.3), (int) (target.getHeight() * 0.3), true);
        }
        else if(size_ <= 0.4){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.4), (int) (target.getHeight() * 0.4), true);
        }
        else if(size_ <= 0.5){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.5), (int) (target.getHeight() * 0.5), true);
        }
        else if(size_ <= 0.6){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.6), (int) (target.getHeight() * 0.6), true);
        }
        else if(size_ <= 0.7){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.7), (int) (target.getHeight() * 0.7), true);
        }
        else if(size_ <= 0.8){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.8), (int) (target.getHeight() * 0.8), true);
        }
        else if(size_ <= 0.9){
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth() * 0.9), (int) (target.getHeight() * 0.9), true);
        }
        else{
            source = Bitmap.createScaledBitmap(source, (int) (target.getWidth()), (int) (target.getHeight()), true);
        }


        Bitmap resultingBitmap = Bitmap.createBitmap(target.getWidth(), target.getHeight(), target.getConfig());

        Canvas canvas = new Canvas(resultingBitmap);
        canvas.drawBitmap(target, new Matrix(), null);
        canvas.drawBitmap(source, (float) ((target.getWidth() - source.getWidth()) * horizontal_), (float) ((target.getHeight() - source.getHeight()) * vertical_), new Paint());

//        Bitmap maskBitmap = Bitmap.createBitmap(target, (int) ((target.getWidth() - source.getWidth()) * horizontal_), (int) ((target.getHeight() - source.getHeight()) * vertical_)
//                , (int) ((target.getHeight() + source.getHeight()) * horizontal_), (int) ((target.getHeight() + source.getHeight()) * vertical_));


        if(needed == 1){
            int A; int R; int G; int B;
            int A2; int R2; int G2; int B2;
            int A3; int R3; int G3; int B3;
            int color, color2, color3;
            for(int i = 0; i < target.getWidth(); i++){
                for(int j = 0; j < target.getHeight(); j++){
                    color = resultingBitmap.getPixel(i,j);
                    color2 = target.getPixel(i,j);
                    if(color == color2) continue;
                    A = (color >> 24) & 0xff; // or color >>> 24
                    R = (color >> 16) & 0xff;
                    G = (color >>  8) & 0xff;
                    B = (color      ) & 0xff;
                    A2 = (color2 >> 24) & 0xff; // or color >>> 24
                    R2 = (color2 >> 16) & 0xff;
                    G2 = (color2 >>  8) & 0xff;
                    B2 = (color2      ) & 0xff;
                    A3 = (A + A2) >> 1;
                    R3 = (R + R2) >> 1;
                    G3 = (G + G2) >> 1;
                    B3 = (B + B2) >> 1;
                    color3 = (A3 << 24) | (R3 << 16) | (G3 << 8) | (B3);
                    resultingBitmap.setPixel(i,j,color3);
                }
            }
        }


        fImage.setImageBitmap(resultingBitmap);

        Mat target_final = new Mat();


//        offset[0] = (target.getHeight() - source.getHeight()) / 2;
//        offset[1] = (target.getWidth() - source.getWidth()) / 2;
//
//        float[][] M = {{1, 0, offset[0]},{0, 1, offset[1]}};
//
//        Mat mat_source = new Mat();
//        Utils.bitmapToMat(source, mat_source);
//
//        Mat mat_M = new Mat();
//        mat_M.put(0,0, M[0]);
//        mat_M.put(1,0, M[1]);
//
//        Mat mat_range = new Mat();
//        mat_range.put(0,0, x_range);
//        mat_range.put(0,0, y_range);
//
//        Imgproc.warpAffine(mat_source, mat_source, mat_M, mat_source.size(), Imgproc.INTER_LINEAR);
//
//        Mat mat_mask = new Mat();
//        Utils.bitmapToMat(mask, mat_mask);
//        mat_mask.submat((int)x_min,(int)x_max, (int)y_min,(int)y_max);
//        for(int i = 0; i < mat_mask.rows(); ++i){
//            for(int j = 0; j < mat_mask.cols(); ++j){
//                double[] data = mat_mask.get(i,j);
//                int flag = 0;
//                for (double datum : data) {
//                    if (datum != 0) {
//                        flag = 1;
//                        break;
//                    }
//                }
//                if(flag == 0){
//                    Arrays.fill(data, 1);
//                }
//            }
//        }
//
//        // mask = cv2.threshold(mask, 127, 1, cv2.THRESH_BINARY)
//        // https://stackoverflow.com/questions/13428689/whats-the-difference-between-cvtype-values-in-opencv
//        int[][] mat_A = laplacian_matrix((int)y_range, (int)x_range);
//        Mat laplacian = new Mat((int) x_range, (int) x_range, CvType.CV_32FC1);
//        for (int i=0;i<x_range;i++)
//            laplacian.put(i,0, mat_A[i]);
//
//
//        // # for \Delta g
//        // laplacian = mat_A.tocsc()
//
////        # set the region outside the mask to identity
//        //        if mask[y, x] == 0:
////        k = x + y * x_range
////        mat_A[k, k] = 1
////        mat_A[k, k + 1] = 0
////        mat_A[k, k - 1] = 0
////        mat_A[k, k + x_range] = 0
////        mat_A[k, k - x_range] = 0
//        for(int i = 1; i < y_range - 1; i++){
//            for(int j = 1; j < x_range - 1; j++){
//                double[] data = mat_mask.get(j,i);
//                int flag = 0;
//                for (double datum : data) {
//                    if (datum != 0) {
//                        flag = 1;
//                        break;
//                    }
//                }
//                if(flag == 0){
//                    int l = (int) (i * j * x_range);
//                    double[] data1 = mat_mask.get(l,l);
//                    Arrays.fill(data1, 1);
//                    laplacian.put(l,l,data1);
//                    double[] data2 = mat_mask.get(l,l+1);
//                    Arrays.fill(data2,0);
//                    laplacian.put(l,l+1,data2);
//                    double[] data3 = mat_mask.get(l,l-1);
//                    Arrays.fill(data3, 0);
//                    laplacian.put(l,l-1,data3);
//                    double[] data4 = mat_mask.get(l, (int) (l + x_range));
//                    Arrays.fill(data4, 0);
//                    laplacian.put(l, (int) (l+x_range),data4);
//                    double[] data5 = mat_mask.get(l, (int) (l - x_range));
//                    Arrays.fill(data5, 0);
//                    laplacian.put(l, (int) (l-x_range),data5);
//                }
//            }
//        }
//
//
////        # corners
////        # mask[0, 0]
////        # mask[0, y_range-1]
////        # mask[x_range-1, 0]
////        # mask[x_range-1, y_range-1]
//
//        // mat_A = mat_A.tocsc()
//
//        Mat mat_mask_flatten = new Mat();
//
//        int cols = 0;
//        for(int i = 0; i < mat_mask.rows(); ++i){
//            for(int j = 0; j < mat_mask.cols(); ++j){
//                double[] data = mat_mask.get(i,j);
//                mat_mask_flatten.put(0, cols, data);
//                cols = cols + 1;
//            }
//        }
//
//        double[] data_check = mat_source.get(0,0);
//        int size = data_check.length;
        Mat final_target = new Mat();
//        for(int p = 0; p < size; p++) {
//            // for channel in range(source.shape[2]):
//            // source_flat = source[y_min:y_max, x_min:x_max, channel].flatten()
//            // target_flat = target[y_min:y_max, x_min:x_max, channel].flatten()
//            Mat mat_source_flatten = new Mat();
//            Mat mat_target_flatten = new Mat();
//            cols = 0;
//            for (int i = (int) y_min; i < (int) y_max; ++i) {
//                for (int j = (int) x_min; j < (int) x_max; ++j) {
//                    double[] data = mat_mask.get(i, j);
//                    mat_source_flatten.put(0, cols, data[p]);
//                    mat_target_flatten.put(0, cols, data[p]);
//                    cols = cols + 1;
//                }
//            }
//
//            int alpha = 1;
//            Mat mat_b = new Mat();
//            mat_b = laplacian.mul(mat_source_flatten);
//            for (int i = 0; i < mat_b.rows(); i++) {
//                for (int j = 0; j < mat_b.cols(); j++) {
//                    double[] data = mat_mask.get(i, j);
//                    for (int k = 0; k < data.length; k++) {
//                        data[k] = data[k] * alpha;
//                    }
//                    mat_b.put(i, j, data);
//                }
//            }
//
//            //        # concat = source_flat*mask_flat + target_flat*(1-mask_flat)
//            //
//            //        # inside the mask:
//            //        # \Delta f = div v = \Delta g
//
//
//            //        # outside the mask:
//            //        # f = t
//
//            for (int i = 0; i < mat_mask_flatten.rows(); i++) {
//                for (int j = 0; j < mat_mask_flatten.cols(); j++) {
//                    double[] data = mat_mask_flatten.get(i, j);
//                    int flag = 0;
//                    for (double datum : data) {
//                        if (datum != 0) {
//                            flag = 1;
//                            break;
//                        }
//                    }
//                    if (flag == 0) {
//                        mat_b.put(i, j, mat_target_flatten.get(i, j));
//                    }
//                }
//            }
//            Mat x = new Mat();
//            x = laplacian.inv().mul(mat_b);
//            //x = spsolve(mat_A mat_b)
//            //        # print(x.shape)
//            x = x.reshape((int) y_range, (int) x_range);
//            //x = x.reshape((y_range, x_range));
//
//            //        # print(x.shape)
//            //x[x > 255] = 255
//            //x[x < 0] = 0
//            for (int i = 0; i < x.rows(); i++) {
//                for (int j = 0; j < x.cols(); j++) {
//                    double[] data = x.get(i, j);
//                    for (double datum : data) {
//                        if (datum > 255) {
//                            datum = 255;
//                        }
//                        else if(datum < 0){
//                            datum = 0;
//                        }
//                    }
//                }
//            }
//
//            // x = x.astype('uint8')
//            //        # x = cv2.normalize(x, alpha=0, beta=255, norm_type=cv2.NORM_MINMAX)
//            //        # print(x.shape)
//
//            //target[y_min:y_max, x_min:x_max, channel] = x;
//            cols = 0;
//            for (int i = (int) y_min; i < (int) y_max; ++i) {
//                for (int j = (int) x_min; j < (int) x_max; ++j) {
//                    double[] data = x.get(i-(int)y_min, j-(int)x_min);
//                    final_target.put(i, j, data);
//                }
//            }
//
//        }
}

}
