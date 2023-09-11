#include <opencv2/opencv.hpp>

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 定义椭圆的参数
	cv::Point center(300, 200); // 椭圆的中心坐标
	cv::Size axes(100, 50);    // 长轴和短轴的长度
	double angle = 30.0;       // 椭圆的旋转角度
	double startAngle = 0.0;   // 起始角度
	double endAngle = 360.0;   // 终止角度
	cv::Scalar color(0, 0, 255); // 红色
	int thickness = 2;

	// 在图像上绘制椭圆
	cv::ellipse(image, center, axes, angle, startAngle, endAngle, color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Ellipse Drawing", image);
	cv::waitKey(0);

	return 0;
}
