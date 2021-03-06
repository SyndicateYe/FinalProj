package com.ece420.lab7;

//import java.awt.image.BufferedImage;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Color;


public class PossionEqSolver
{
    private static final int MASK_INSIDE = -1;
    private static final int MASK_BORDER = -2;
    private static final int MASK_OUTSIDE = -3;
    private static final int MAX_ITERATIONS = 100000;

    private Bitmap m_targetImage;
    private Bitmap m_cutImage;

    private ArrayList<Point2> m_cutPoints;
 //   ArrayList<Point> m_cutPoints = new ArrayList<Point>();
    private int[][] m_mask;
    private int m_imageX;
    private int m_imageY;

    private int m_n;
    // Decomposed A: A = D + R
    private int[] m_D;
    private int[][] m_R;
    private double[][] m_x;
    private double[][] m_nextX;
    private double [][] m_prevX;
    private double[][] m_b;

    public PossionEqSolver(Bitmap targetImage, Bitmap cutImage, ArrayList<Point2> cutPoints, int[][] mask,
                  int imageX, int imageY, boolean mixedGradients)
    {
        m_targetImage = targetImage;
        m_cutImage = cutImage;
        m_cutPoints = cutPoints;
        m_mask = mask;
        m_imageX = imageX;
        m_imageY = imageY;

        m_n = cutPoints.size();

        m_D = new int[m_n];
        m_R = new int[m_n][4];
        m_x = new double[m_n][3];
        m_nextX = new double[m_n][3];
        m_prevX = new double[m_n][3];
        m_b = new double[m_n][3];

        initMatrix(mixedGradients);
    }

    private int colorR(int rgb)
    {
        return (rgb & 0xFF0000) >> 16;
    }

    private int colorG(int rgb)
    {
        return (rgb & 0xFF00) >> 8;
    }

    private int colorB(int rgb)
    {
        return rgb & 0xFF;
    }

    private void initMatrix(boolean mixedGradients)
    {
        Point2[] neighbors = {
                new Point2(-1, 0), new Point2(1, 0),
                new Point2(0, -1), new Point2(0, 1)};

        for (int i = 0; i < m_n; i++)
        {
            m_b[i][0] = 0.0;
            m_b[i][1] = 0.0;
            m_b[i][2] = 0.0;

            Point2 p = m_cutPoints.get(i);
            int gp_red = Color.red(m_cutImage.getPixel(p.x, p.y));
            int gp_green = Color.green(m_cutImage.getPixel(p.x, p.y));
            int gp_blue = Color.blue(m_cutImage.getPixel(p.x, p.y));
            int fp_red = Color.red(m_targetImage.getPixel(p.x + m_imageX, p.y + m_imageY));
            int fp_green = Color.green(m_targetImage.getPixel(p.x + m_imageX, p.y + m_imageY));
            int fp_blue = Color.blue(m_targetImage.getPixel(p.x + m_imageX, p.y + m_imageY));
            int num_neighbors = 0;
            for (int j = 0; j < 4; j++)
            {
                Point2 np = p.add(neighbors[j]);
                np.x += m_imageX;
                np.y += m_imageY;
                m_R[i][j] = -1;

                if (np.x < 1 || np.x >= m_mask.length - 1 ||
                        np.y < 1 || np.y >= m_mask[0].length - 1)
                {
                    continue;
                }

                num_neighbors++;
                int type = m_mask[np.x][np.y];
                if (type == MASK_BORDER)
                {
                    int red = Color.red(m_targetImage.getPixel(np.x, np.y));
                    int green = Color.green(m_targetImage.getPixel(np.x, np.y));
                    int blue = Color.blue(m_targetImage.getPixel(np.x, np.y));
                    m_b[i][0] += red;
                    m_b[i][1] += green;
                    m_b[i][2] += blue;
                }
                else
                {
                    m_R[i][j] = type;
                    int gq_red = Color.red(m_cutImage.getPixel(np.x - m_imageX, np.y - m_imageY));
                    int gq_green = Color.green(m_cutImage.getPixel(np.x - m_imageX, np.y - m_imageY));
                    int gq_blue = Color.blue(m_cutImage.getPixel(np.x - m_imageX, np.y - m_imageY));
                    int fq_red = Color.red(m_targetImage.getPixel(np.x, np.y));
                    int fq_green = Color.green(m_targetImage.getPixel(np.x, np.y));
                    int fq_blue = Color.blue(m_targetImage.getPixel(np.x, np.y));

                    if (mixedGradients)
                    {
                        int df = fp_red - fq_red;
                        int dg = gp_red - gq_red;
                        m_b[i][0] += (Math.abs(df) > Math.abs(dg)) ? df : dg;
                        df = fp_green - fq_green;
                        dg = gp_green - gq_green;
                        m_b[i][1] += (Math.abs(df) > Math.abs(dg)) ? df : dg;
                        df = fp_blue - fq_blue;
                        dg = gp_blue - gq_blue;
                        m_b[i][2] += (Math.abs(df) > Math.abs(dg)) ? df : dg;
                    }
                    else
                    {
                        m_b[i][0] += gp_red - gq_red;
                        m_b[i][1] += gp_green - gq_green;
                        m_b[i][2] += gp_blue - gq_blue;
                    }
                }
            }

            m_D[i] = num_neighbors;
        }
    }

    private double getError()
    {
        double totalE = 0.0;
        double[] e = new double[3];
        for (int i = 0; i < m_n; i++)
        {
            e[0] = m_b[i][0];
            e[1] = m_b[i][1];
            e[2] = m_b[i][2];

            for (int j = 0; j < 4; j++)
            {
                if (m_R[i][j] > -1)
                {
                    int idx = m_R[i][j];
                    e[0] += m_x[idx][0];
                    e[1] += m_x[idx][1];
                    e[2] += m_x[idx][2];
                }
            }
            e[0] -= m_D[i] * m_x[i][0];
            e[1] -= m_D[i] * m_x[i][1];
            e[2] -= m_D[i] * m_x[i][2];
            totalE += e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
        }
        return Math.sqrt(totalE);
    }

    private void iterateJacobi()
    {
        for (int i = 0; i < m_n; i++)
        {
            m_nextX[i][0] = m_b[i][0];
            m_nextX[i][1] = m_b[i][1];
            m_nextX[i][2] = m_b[i][2];

            for (int j = 0; j < 4; j++)
            {
                if (m_R[i][j] > -1)
                {
                    int idx = m_R[i][j];
                    m_nextX[i][0] += m_x[idx][0];
                    m_nextX[i][1] += m_x[idx][1];
                    m_nextX[i][2] += m_x[idx][2];
                }
            }
            double invD = 1.0 / (double)m_D[i];
            m_nextX[i][0] *= invD;
            m_nextX[i][1] *= invD;
            m_nextX[i][2] *= invD;
        }

        for (int i = 0; i < m_n; i++)
        {
            m_x[i] = m_nextX[i];
        }
    }

    private void iterateRelax()
    {
        // http://disq.us/p/hvps4v
        double omega = 1.95;
        for (int i = 0; i < m_n; i++)
        {
            for (int k = 0; k < 3; k++)
            {
                m_prevX[i][k] = m_x[i][k];
                m_x[i][k] = m_b[i][k];
            }

            for (int n = 0; n < 4; n++)
            {
                if (m_R[i][n] > -1)
                {
                    int idx = m_R[i][n];
                    m_x[i][0] += m_x[idx][0];
                    m_x[i][1] += m_x[idx][1];
                    m_x[i][2] += m_x[idx][2];
                }
            }

            for (int k = 0; k < 3; k++)
            {
                m_x[i][k] = m_prevX[i][k] + omega * (m_x[i][k] / m_D[i] - m_prevX[i][k]);
            }
        }
    }

    public void run()
    {
        long start = System.nanoTime();
        int i = 0;
        double error = 0.0;
        do
        {
            error = getError();
            i++;
            iterateRelax();
        }
        while (error > 1.0 && i < MAX_ITERATIONS);

        if (i >= MAX_ITERATIONS)
        {
            System.out.println("Convergence error: " + error);
        }
        double time = (System.nanoTime() - start) / 1e9;
        System.out.println("Solve time: " + time + "s");
        System.out.println("Pixels blended: " + m_n);
        System.out.println("Iterations: " + i);
    }

    public Bitmap updateTarget()
    {
        for (int i = 0; i < m_n; i++)
        {
            Point2 p = m_cutPoints.get(i);

            int R = (int)Math.round(m_x[i][0]);
            int G = (int)Math.round(m_x[i][1]);
            int B = (int)Math.round(m_x[i][2]);

            R = Math.max(Math.min(R, 255), 0);
            G = Math.max(Math.min(G, 255), 0);
            B = Math.max(Math.min(B, 255), 0);
            int rgb = (R << 16) & 0xFF0000 | (G << 8) & 0x00FF00 | B & 0x0000FF;

            m_targetImage.setPixel(p.x + m_imageX, p.y + m_imageY, rgb);
        }
        return m_targetImage;
    }

}