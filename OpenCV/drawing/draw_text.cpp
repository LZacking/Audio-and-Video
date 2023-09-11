#include <opencv2/opencv.hpp>

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 设置文本的字体和大小
	int fontFace = cv::FONT_HERSHEY_SIMPLEX; // 字体类型
	double fontScale = 2.0; // 字体大小
	int thickness = 3; // 文本线宽

	// 设置文本的位置和内容
	cv::Point textPosition(100, 200); // 文本起始位置
	std::string text = "Hello, OpenCV!";

	// 设置文本的颜色
	cv::Scalar textColor(0, 0, 255); // 红色

	// 使用指定的字体、大小、颜色等参数绘制文本
	cv::putText(image, text, textPosition, fontFace, fontScale, textColor, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Text Drawing", image);
	cv::waitKey(0);

	return 0;
}
