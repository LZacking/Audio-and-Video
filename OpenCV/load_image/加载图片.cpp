#include <opencv2/opencv.hpp>
#include <iostream>

// 加载图片
int main() {
	// 定义图片文件的完整路径
	std::string image_path = "D:\\path_to_your_image.jpg";

	// 使用OpenCV加载图片
	cv::Mat image = cv::imread(image_path);

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	cv::imshow("Loaded Image", image);
	cv::waitKey(0);

	return 0;
}
