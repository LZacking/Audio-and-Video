#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// 读取图像
	cv::Mat image = cv::imread("D:\\灯牌大战.jpg");

	if (image.empty()) {
		std::cout << "Error loading image." << std::endl;
		return -1;
	}

	// 计算每个颜色通道的像素数
	int totalPixels = image.rows * image.cols;

	int visibleBluePixels = 0;
	int visibleGreenPixels = 0;
	int visibleRedPixels = 0;

	// 遍历图像像素，计算肉眼可见的颜色占比
	for (int row = 0; row < image.rows; ++row) {
		for (int col = 0; col < image.cols; ++col) {
			cv::Vec3b pixel = image.at<cv::Vec3b>(row, col);

			// 根据人眼感知的颜色范围，判断像素是否为肉眼可见的颜色
			if (pixel[2] > 50 && pixel[2] > pixel[1] && pixel[2] > pixel[0]) {  // 判断红色
				visibleRedPixels++;
			}
			else if (pixel[1] > 50 && pixel[1] > pixel[0] && pixel[1] > pixel[2]) {  // 判断绿色
				visibleGreenPixels++;
			}
			else if (pixel[0] > 50 && pixel[0] > pixel[1] && pixel[0] > pixel[2]) {  // 判断蓝色
				visibleBluePixels++;
			}
		}
	}

	// 计算占比
	double visibleBluePercentage = static_cast<double>(visibleBluePixels) / totalPixels * 100;
	double visibleGreenPercentage = static_cast<double>(visibleGreenPixels) / totalPixels * 100;
	double visibleRedPercentage = static_cast<double>(visibleRedPixels) / totalPixels * 100;

	// 打印结果
	std::cout << "易烊千玺（红色）: " << visibleRedPercentage << "%" << std::endl;
	std::cout << "王俊凯（蓝色）: " << std::fixed << std::setprecision(2) << visibleBluePercentage << "%" << std::endl;
	std::cout << "王源（绿色）: " << visibleGreenPercentage << "%" << std::endl;
	

	return 0;
}
