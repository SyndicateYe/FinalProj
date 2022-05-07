//package com.ece420.lab7;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.Point;
//import org.opencv.core.Rect2d;
//import org.opencv.core.Scalar;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.photo.Photo;
//import org.opencv.android.Utils;
//
//public class PoissionEditing {
//    void computeGradientX(Mat img, Mat gx)
//    {
//        Mat kernel;
//        kernel = Mat.zeros(1, 3, CvType.CV_8S);
//        kernel.put(0,2,1);
//        kernel.put(0,1,-1);
//
//        if(img.channels() == 3)
//        {
//            Imgproc.filter2D(img, gx, CvType.CV_32F, kernel);
//        }
//        else if (img.channels() == 1)
//        {
//            Imgproc.filter2D(img, gx, CvType.CV_32F, kernel);
//            Imgproc.cvtColor(gx, gx, Imgproc.COLOR_GRAY2BGR);
//        }
//    }
//
//    void computeGradientY(Mat img, Mat gy)
//    {
//        Mat kernel;
//        kernel = Mat.zeros(3, 1, CvType.CV_8S);
//        kernel.put(2,0,1);
//        kernel.put(1,0,-1);
//
//        if(img.channels() == 3)
//        {
//            Imgproc.filter2D(img, gy, CvType.CV_32F, kernel);
//        }
//        else if (img.channels() == 1)
//        {
//            Imgproc.filter2D(img, gy, CvType.CV_32F, kernel);
//            Imgproc.cvtColor(gy, gy, Imgproc.COLOR_GRAY2BGR);
//        }
//    }
//
//    void computeLaplacianX(Mat img, Mat laplacianX)
//    {
//        Mat kernel;
//        kernel = Mat.zeros(1, 3, CvType.CV_8S);
//        kernel.put(0,0,-1);
//        kernel.put(0,1,1);
//        Imgproc.filter2D(img, laplacianX, CvType.CV_32F, kernel);
//    }
//
//    void computeLaplacianY(Mat img, Mat laplacianY)
//    {
//        Mat kernel;
//        kernel = Mat.zeros(3, 1, CvType.CV_8S);
//        kernel.put(0,0,-1);
//        kernel.put(1,0,1);
//        Imgproc.filter2D(img, laplacianY, CvType.CV_32F, kernel);
//    }
//
//    void dst(Mat src, Mat dest, boolean invert)
//    {
//        Mat temp = Mat.zeros(src.rows(), 2*src.cols()+2, CvType.CV_32F);
//
//        int flag = invert ? Core.DFT_ROWS + Core.DFT_SCALE + Core.DFT_INVERSE: Core.DFT_ROWS;
//
//        src.copyTo(temp(Rect(1,0, src.cols(), src.rows())));
//
//        for(int j = 0 ; j < src.rows() ; ++j)
//        {
//            float * tempLinePtr = temp.ptr<float>(j);
//            float * srcLinePtr = src.ptr<float>(j);
//            for(int i = 0 ; i < src.cols() ; ++i)
//            {
//                tempLinePtr[src.cols() + 2 + i] = - srcLinePtr[src.cols() - 1 - i];
//            }
//        }
//
//        Mat planes[] = {temp, Mat.zeros(temp.size(), CvType.CV_32F)};
//        Mat complex;
//
//        Core.merge(planes, 2, complex);
//        Core.dft(complex, complex, flag);
//        Core.split(complex, planes);
//        temp = Mat.zeros(src.cols(), 2 * src.rows() + 2, CvType.CV_32F);
//
//        for(int j = 0 ; j < src.cols() ; ++j)
//        {
//            float * tempLinePtr = temp.ptr<float>(j);
//            for(int i = 0 ; i < src.rows() ; ++i)
//            {
//                float val = planes[1].ptr<float>(i)[j + 1];
//                tempLinePtr[i + 1] = val;
//                tempLinePtr[temp.cols() - 1 - i] = - val;
//            }
//        }
//
//        Mat planes2[] = {temp, Mat::zeros(temp.size(), CvType.CV_32F)};
//
//        Core.merge(planes2, 2, complex);
//        Core.dft(complex, complex, flag);
//        Core.split(complex, planes2);
//
//        temp = planes2[1].t();
//        temp(Rect( 0, 1, src.cols(), src.rows())).copyTo(dest);
//    }
//
//    void solve( Mat img, Mat mod_diff, Mat result)
//    {
//        int w = img.cols();
//        int h = img.rows();
//
//        Mat res;
//        dst(mod_diff, res);
//
//        for(int j = 0 ; j < h-2; j++)
//        {
//            float * resLinePtr = res.ptr<float>(j);
//            for(int i = 0 ; i < w-2; i++)
//            {
//                resLinePtr[i] /= (filter_X[i] + filter_Y[j] - 4);
//            }
//        }
//
//        dst(res, mod_diff, true);
//
//        unsigned char *  resLinePtr = result.ptr<unsigned char>(0);
//        unsigned char * imgLinePtr = img.ptr<unsigned char>(0);
//        float * interpLinePtr = NULL;
//
//        //first col
//        for(int i = 0 ; i < w ; ++i)
//            result.ptr<unsigned char>(0)[i] = img.ptr<unsigned char>(0)[i];
//
//        for(int j = 1 ; j < h-1 ; ++j)
//        {
//            resLinePtr = result.ptr<unsigned char>(j);
//            imgLinePtr  = img.ptr<unsigned char>(j);
//            interpLinePtr = mod_diff.ptr<float>(j-1);
//
//            //first row
//            resLinePtr[0] = imgLinePtr[0];
//
//            for(int i = 1 ; i < w-1 ; ++i)
//            {
//                //saturate cast is not used here, because it behaves differently from the previous implementation
//                //most notable, saturate_cast rounds before truncating, here it's the opposite.
//                float value = interpLinePtr[i-1];
//                if(value < 0.)
//                    resLinePtr[i] = 0;
//                else if (value > 255.0)
//                    resLinePtr[i] = 255;
//                else
//                    resLinePtr[i] = static_cast<unsigned char>(value);
//            }
//
//            //last row
//            resLinePtr[w-1] = imgLinePtr[w-1];
//        }
//
//        //last col
//        resLinePtr = result.ptr<unsigned char>(h-1);
//        imgLinePtr = img.ptr<unsigned char>(h-1);
//        for(int i = 0 ; i < w ; ++i)
//            resLinePtr[i] = imgLinePtr[i];
//    }
//
//    void poissonSolver(Mat img, Mat laplacianX , Mat laplacianY, Mat result)
//    {
//        int w = img.cols();
//        int h = img.rows();
//
//        Mat lap = laplacianX + laplacianY;
//
//        Mat bound = img.clone();
//
//        rectangle(bound, Point(1, 1), Point(img.cols(-2, img.rows(-2), Scalar::all(0), -1);
//        Mat boundary_points;
//        Laplacian(bound, boundary_points, CvType.CV_32F);
//
//        boundary_points = lap - boundary_points;
//
//        Mat mod_diff = boundary_points(Rect(1, 1, w-2, h-2));
//
//        solve(img,mod_diff,result);
//    }
//
//    void initVariables( Mat destination,  Mat binaryMask)
//    {
//        Mat destinationGradientX = new Mat(destination.size(),CvType.CV_32FC3);
//        Mat destinationGradientY = new Mat(destination.size(),CvType.CV_32FC3);
//        Mat patchGradientX = new Mat(destination.size(),CvType.CV_32FC3);
//        Mat patchGradientY = new Mat(destination.size(),CvType.CV_32FC3);
//
//        Mat binaryMaskFloat = new Mat(binaryMask.size(),CvType.CV_32FC3);
//        Mat binaryMaskFloatInverted = new Mat(binaryMask.size(),CvType.CV_32FC3);
//
//        //init of the filters used in the dst
//        int w = destination.cols();
//        filter_X.resize(w - 2);
//        double scale = Math.PI / (w - 1);
//        for(int i = 0 ; i < w-2 ; ++i)
//            filter_X[i] = 2.0f * (float)std::cos(scale * (i + 1));
//
//        int h  = destination.rows();
//        filter_Y.resize(h - 2);
//        scale = Math.PI / (h - 1);
//        for(int j = 0 ; j < h - 2 ; ++j)
//            filter_Y[j] = 2.0f * (float)std::cos(scale * (j + 1));
//    }
//
//    void computeDerivatives( Mat destination,  Mat patch,  Mat binaryMask)
//    {
//        initVariables(destination, binaryMask);
//
//        computeGradientX(destination, destinationGradientX);
//        computeGradientY(destination, destinationGradientY);
//
//        computeGradientX(patch, patchGradientX);
//        computeGradientY(patch, patchGradientY);
//
//        Mat Kernel(Size(3, 3), CvType.CV_8UC1);
//        Kernel.setTo(Scalar(1));
//        erode(binaryMask, binaryMask, Kernel, Point(-1,-1), 3);
//
//        binaryMask.convertTo(binaryMaskFloat, CvType.CV_32FC1, 1.0/255.0);
//    }
//
//    void scalarProduct(Mat mat, float r, float g, float b)
//    {
//        vector <Mat> channels;
//        Core.split(mat,channels);
//        Core.multiply(channels[2],r,channels[2]);
//        Core.multiply(channels[1],g,channels[1]);
//        Core.multiply(channels[0],b,channels[0]);
//        Core.merge(channels,mat);
//    }
//
//    void arrayProduct(Mat lhs,  Mat rhs, Mat result)
//    {
//        vector <Mat> lhs_channels;
//        vector <Mat> result_channels;
//
//        Core.split(lhs,lhs_channels);
//        Core.split(result,result_channels);
//
//        for(int chan = 0 ; chan < 3 ; ++chan)
//            Core.multiply(lhs_channels[chan],rhs,result_channels[chan]);
//
//        Core.merge(result_channels,result);
//    }
//
//    void poisson( Mat destination)
//    {
//        Mat laplacianX = destinationGradientX + patchGradientX;
//        Mat laplacianY = destinationGradientY + patchGradientY;
//
//        computeLaplacianX(laplacianX,laplacianX);
//        computeLaplacianY(laplacianY,laplacianY);
//
//        Core.split(laplacianX,rgbx_channel);
//        Core.split(laplacianY,rgby_channel);
//
//        Core.split(destination,output);
//
//        for(int chan = 0 ; chan < 3 ; ++chan)
//        {
//            poissonSolver(output[chan], rgbx_channel[chan], rgby_channel[chan], output[chan]);
//        }
//    }
//
//    void evaluate( Mat I,  Mat wmask,  Mat cloned)
//    {
//        Core.bitwise_not(wmask,wmask);
//
//        wmask.convertTo(binaryMaskFloatInverted,CvType.CV_32FC1,1.0/255.0);
//
//        arrayProduct(destinationGradientX, binaryMaskFloatInverted, destinationGradientX);
//        arrayProduct(destinationGradientY, binaryMaskFloatInverted, destinationGradientY);
//
//        poisson(I);
//
//        Core.merge(output,cloned);
//    }
//
//    void normalClone( Mat destination,  Mat patch,  Mat binaryMask, Mat cloned, int flag)
//    {
//        int w = destination.cols();
//        int h = destination.rows();
//        int channel = destination.channels();
//        int n_elem_in_line = w * channel;
//
//        computeDerivatives(destination,patch,binaryMask);
//
//        switch(flag)
//        {
//            case NORMAL_CLONE:
//                arrayProduct(patchGradientX, binaryMaskFloat, patchGradientX);
//                arrayProduct(patchGradientY, binaryMaskFloat, patchGradientY);
//                break;
//
//            case MIXED_CLONE:
//            {
//                AutoBuffer<int> maskIndices(n_elem_in_line);
//                for (int i = 0; i < n_elem_in_line; ++i)
//                    maskIndices[i] = i / channel;
//
//                for(int i=0;i < h; i++)
//                {
//                    float * patchXLinePtr = patchGradientX.ptr<float>(i);
//                    float * patchYLinePtr = patchGradientY.ptr<float>(i);
//                    float * destinationXLinePtr = destinationGradientX.ptr<float>(i);
//                    float * destinationYLinePtr = destinationGradientY.ptr<float>(i);
//                    float * binaryMaskLinePtr = binaryMaskFloat.ptr<float>(i);
//
//                    for(int j=0; j < n_elem_in_line; j++)
//                    {
//                        int maskIndex = maskIndices[j];
//
//                        if(abs(patchXLinePtr[j] - patchYLinePtr[j]) >
//                                abs(destinationXLinePtr[j] - destinationYLinePtr[j]))
//                        {
//                            patchXLinePtr[j] *= binaryMaskLinePtr[maskIndex];
//                            patchYLinePtr[j] *= binaryMaskLinePtr[maskIndex];
//                        }
//                        else
//                        {
//                            patchXLinePtr[j] = destinationXLinePtr[j]
//                                    * binaryMaskLinePtr[maskIndex];
//                            patchYLinePtr[j] = destinationYLinePtr[j]
//                                    * binaryMaskLinePtr[maskIndex];
//                        }
//                    }
//                }
//            }
//            break;
//
//        }
//
//        evaluate(destination,binaryMask,cloned);
//    }
//}
