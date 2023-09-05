#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// 读取图像
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// 定义模糊核的大小
	cv::Size kernelSize(5, 5); // 可以根据需要调整核的大小

	// 用来存放模糊之后的图片
	cv::Mat blurredImage;
	// 执行高斯模糊操作
	cv::GaussianBlur(image, blurredImage, kernelSize, 0);

	// 显示原始图像和模糊后的图像
	cv::imshow("原始图像", image);
	cv::imshow("模糊后的图像", blurredImage);

	// 等待用户按下键盘上的任意键
	cv::waitKey(0);

	return 0;
}
