/*====================================================================*
 -  Copyright (C) 2001 Leptonica.  All rights reserved.
 -  This software is distributed in the hope that it will be
 -  useful, but with NO WARRANTY OF ANY KIND.
 -  No author or distributor accepts responsibility to anyone for the
 -  consequences of using this software, or for whether it serves any
 -  particular purpose or works at all, unless he or she says so in
 -  writing.  Everyone is granted permission to copy, modify and
 -  redistribute this source code, for commercial or non-commercial
 -  purposes, with the following restrictions: (1) the origin of this
 -  source code must not be misrepresented; (2) modified versions must
 -  be plainly marked as such; and (3) this notice may not be removed
 -  or altered from any source or modified source distribution.
 *====================================================================*/

/*
 *  fpix_reg.c
 *
 *    Regression test for a number of functions in the FPix utility.
 *    FPix allows you to do floating point operations such as
 *    convolution, with conversions to and from Pix.
 */

#include <math.h>
#ifndef _WIN32
#include <unistd.h>
#else
#include <windows.h>   /* for Sleep() */
#endif  /* _WIN32 */
#include "allheaders.h"

main(int    argc,
char **argv)
{
l_float32     sum, sumx, sumy, diff;
FPIX         *fpixs, *fpixs2, *fpixs3, *fpixt1, *fpixt2, *fpixg, *fpixd;
L_KERNEL     *kel, *kelx, *kely;
PIX          *pixs, *pixs2, *pixs3, *pixt, *pixd, *pixg;
PIX          *pixt1, *pixt2, *pixt3, *pixt4, *pixt5, *pixt6;
PIXA         *pixa;
L_REGPARAMS  *rp;

    if (regTestSetup(argc, argv, &rp))
        return 1;

    pixa = pixaCreate(0);

        /* Gaussian kernel */
    kel = makeGaussianKernel(5, 5, 3.0, 4.0);
    kernelGetSum(kel, &sum);
    if (rp->display) fprintf(stderr, "Sum for 2d gaussian kernel = %f\n", sum);
    pixt = kernelDisplayInPix(kel, 41, 2);
    regTestWritePixAndCheck(rp, pixt, IFF_PNG);  /* 0 */
    pixSaveTiled(pixt, pixa, 1, 1, 20, 8);
    pixDestroy(&pixt);

        /* Separable gaussian kernel */
    makeGaussianKernelSep(5, 5, 3.0, 4.0, &kelx, &kely);
    kernelGetSum(kelx, &sumx);
    if (rp->display) fprintf(stderr, "Sum for x gaussian kernel = %f\n", sumx);
    kernelGetSum(kely, &sumy);
    if (rp->display) fprintf(stderr, "Sum for y gaussian kernel = %f\n", sumy);
    if (rp->display) fprintf(stderr, "Sum for x * y gaussian kernel = %f\n",
                         sumx * sumy);
    pixt = kernelDisplayInPix(kelx, 41, 2);
    regTestWritePixAndCheck(rp, pixt, IFF_PNG);  /* 1 */
    pixSaveTiled(pixt, pixa, 1, 0, 20, 8);
    pixDestroy(&pixt);
    pixt = kernelDisplayInPix(kely, 41, 2);
    regTestWritePixAndCheck(rp, pixt, IFF_PNG);  /* 2 */
    pixSaveTiled(pixt, pixa, 1, 0, 20, 8);
    pixDestroy(&pixt);

        /* Use pixRasterop() to generate source image */
    pixs = pixRead("test8.jpg");
    pixs2 = pixRead("karen8.jpg");
    pixRasterop(pixs, 150, 125, 150, 100, PIX_SRC, pixs2, 75, 100);
    regTestWritePixAndCheck(rp, pixs, IFF_JFIF_JPEG);  /* 3 */

        /* Convolution directly with pix */
    pixt1 = pixConvolve(pixs, kel, 8, 1);
    regTestWritePixAndCheck(rp, pixt1, IFF_JFIF_JPEG);  /* 4 */
    pixSaveTiled(pixt1, pixa, 1, 1, 20, 8);
    pixt2 = pixConvolveSep(pixs, kelx, kely, 8, 1);
    regTestWritePixAndCheck(rp, pixt2, IFF_JFIF_JPEG);  /* 5 */
    pixSaveTiled(pixt2, pixa, 1, 0, 20, 8);

        /* Convolution indirectly with fpix, using fpixRasterop()
         * to generate the source image. */
    fpixs = pixConvertToFPix(pixs, 3);
    fpixs2 = pixConvertToFPix(pixs2, 3);
    fpixRasterop(fpixs, 150, 125, 150, 100, fpixs2, 75, 100);
    fpixt1 = fpixConvolve(fpixs, kel, 1);
    pixt3 = fpixConvertToPix(fpixt1, 8, L_CLIP_TO_ZERO, 1);
    regTestWritePixAndCheck(rp, pixt3, IFF_JFIF_JPEG);  /* 6 */
    pixSaveTiled(pixt3, pixa, 1, 1, 20, 8);
    fpixt2 = fpixConvolveSep(fpixs, kelx, kely, 1);
    pixt4 = fpixConvertToPix(fpixt2, 8, L_CLIP_TO_ZERO, 1);
    regTestWritePixAndCheck(rp, pixt4, IFF_JFIF_JPEG);  /* 7 */
    pixSaveTiled(pixt4, pixa, 1, 0, 20, 8);
    pixDestroy(&pixs2);
    fpixDestroy(&fpixs2);
    fpixDestroy(&fpixt1);
    fpixDestroy(&fpixt2);

        /* Comparison of results */
    pixCompareGray(pixt1, pixt2, L_COMPARE_ABS_DIFF, 0, NULL,
                   &diff, NULL, NULL);
    if (rp->display)
        fprintf(stderr, "Ave diff of pixConvolve and pixConvolveSep: %f\n",
                diff);
    pixCompareGray(pixt3, pixt4, L_COMPARE_ABS_DIFF, 0, NULL,
                   &diff, NULL, NULL);
    if (rp->display)
        fprintf(stderr, "Ave diff of fpixConvolve and fpixConvolveSep: %f\n",
                diff);
    pixCompareGray(pixt1, pixt3, L_COMPARE_ABS_DIFF, 0, NULL,
                   &diff, NULL, NULL);
    if (rp->display)
        fprintf(stderr, "Ave diff of pixConvolve and fpixConvolve: %f\n", diff);
    pixCompareGray(pixt2, pixt4, L_COMPARE_ABS_DIFF, GPLOT_PNG, NULL,
                   &diff, NULL, NULL);
    if (rp->display)
        fprintf(stderr, "Ave diff of pixConvolveSep and fpixConvolveSep: %f\n",
                diff);
    pixDestroy(&pixt1);
    pixDestroy(&pixt2);
    pixDestroy(&pixt3);
    pixDestroy(&pixt4);

        /* Test arithmetic operations; add in a fraction rotated by 180 */
    pixs3 = pixRotate180(NULL, pixs);
    regTestWritePixAndCheck(rp, pixs3, IFF_JFIF_JPEG);  /* 8 */
    pixSaveTiled(pixs3, pixa, 1, 1, 20, 8);
    fpixs3 = pixConvertToFPix(pixs3, 3);
    fpixd = fpixLinearCombination(NULL, fpixs, fpixs3, 20.0, 5.0);
    fpixAddMultConstant(fpixd, 0.0, 23.174);   /* multiply up in magnitude */
    pixd = fpixDisplayMaxDynamicRange(fpixd);  /* bring back to 8 bpp */
    regTestWritePixAndCheck(rp, pixd, IFF_JFIF_JPEG);  /* 9 */
    pixSaveTiled(pixd, pixa, 1, 0, 20, 8);
    pixDestroy(&pixs3);
    fpixDestroy(&fpixs3);
    fpixDestroy(&fpixd);
    pixDestroy(&pixd);
    pixDestroy(&pixs);
    fpixDestroy(&fpixs);

        /* Save the comparison graph; gnuplot should have made it by now! */
#ifndef _WIN32
    sleep(2);
#else
    Sleep(2000);
#endif  /* _WIN32 */
    pixt5 = pixRead("/tmp/grayroot.png");
    regTestWritePixAndCheck(rp, pixt5, IFF_PNG);  /* 10 */
    pixSaveTiled(pixt5, pixa, 1, 1, 20, 8);
    pixDestroy(&pixt5);

        /* Display results */
    pixd = pixaDisplay(pixa, 0, 0);
    regTestWritePixAndCheck(rp, pixd, IFF_JFIF_JPEG);  /* 11 */
    pixDisplayWithTitle(pixd, 100, 100, NULL, rp->display);
    pixDestroy(&pixd);
    pixaDestroy(&pixa);

        /* Test some more convolutions, with sampled output. First on pix */
    pixa = pixaCreate(0);
    pixs = pixRead("1555-7.jpg");
    pixg = pixConvertTo8(pixs, 0);
    l_setConvolveSampling(5, 5);
    pixt1 = pixConvolve(pixg, kel, 8, 1);
    regTestWritePixAndCheck(rp, pixt1, IFF_JFIF_JPEG);  /* 12 */
    pixSaveTiled(pixt1, pixa, 1, 1, 20, 32);
    pixt2 = pixConvolveSep(pixg, kelx, kely, 8, 1);
    regTestWritePixAndCheck(rp, pixt2, IFF_JFIF_JPEG);  /* 13 */
    pixSaveTiled(pixt2, pixa, 1, 0, 20, 32);
    pixt3 = pixConvolveRGB(pixs, kel);
    regTestWritePixAndCheck(rp, pixt3, IFF_JFIF_JPEG);  /* 14 */
    pixSaveTiled(pixt3, pixa, 1, 0, 20, 32);
    pixt4 = pixConvolveRGBSep(pixs, kelx, kely);
    regTestWritePixAndCheck(rp, pixt4, IFF_JFIF_JPEG);  /* 15 */
    pixSaveTiled(pixt4, pixa, 1, 0, 20, 32);

        /* Then on fpix */
    fpixg = pixConvertToFPix(pixg, 1);
    fpixt1 = fpixConvolve(fpixg, kel, 1);
    pixt5 = fpixConvertToPix(fpixt1, 8, L_CLIP_TO_ZERO, 0);
    regTestWritePixAndCheck(rp, pixt5, IFF_JFIF_JPEG);  /* 16 */
    pixSaveTiled(pixt5, pixa, 1, 1, 20, 32);
    fpixt2 = fpixConvolveSep(fpixg, kelx, kely, 1);
    pixt6 = fpixConvertToPix(fpixt2, 8, L_CLIP_TO_ZERO, 0);
    regTestWritePixAndCheck(rp, pixt6, IFF_JFIF_JPEG);  /* 17 */
    pixSaveTiled(pixt2, pixa, 1, 0, 20, 32);
    regTestCompareSimilarPix(rp, pixt1, pixt5, 2, 0.00, 0);  /* 18 */
    regTestCompareSimilarPix(rp, pixt2, pixt6, 2, 0.00, 0);  /* 19 */
    pixDestroy(&pixt1);
    pixDestroy(&pixt2);
    pixDestroy(&pixt3);
    pixDestroy(&pixt4);
    pixDestroy(&pixt5);
    pixDestroy(&pixt6);
    fpixDestroy(&fpixg);
    fpixDestroy(&fpixt1);
    fpixDestroy(&fpixt2);

    pixd = pixaDisplay(pixa, 0, 0);
    regTestWritePixAndCheck(rp, pixd, IFF_JFIF_JPEG);  /* 20 */
    pixDisplayWithTitle(pixd, 600, 100, NULL, rp->display);
    pixDestroy(&pixd);
    pixaDestroy(&pixa);

    regTestCleanup(rp);
    pixDestroy(&pixs);
    pixDestroy(&pixg);
    kernelDestroy(&kel);
    kernelDestroy(&kelx);
    kernelDestroy(&kely);
    return 0;
}
