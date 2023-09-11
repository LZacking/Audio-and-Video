#include <opencv2/opencv.hpp>

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 定义多边形的顶点
	std::vector<cv::Point> points;
	points.push_back(cv::Point(100, 100));
	points.push_back(cv::Point(300, 100));
	points.push_back(cv::Point(400, 300));
	points.push_back(cv::Point(200, 400));

	// 将多边形的第一个点复制到最后一个点，以闭合多边形
	points.push_back(points[0]);

	// 设置多边形的颜色（BGR格式）和线宽
	cv::Scalar color(0, 0, 255); // 红色
	int thickness = 2;

	// 使用指定的顶点、颜色和线宽绘制多边形
	std::vector<std::vector<cv::Point>> contours;
	contours.push_back(points);
	cv::polylines(image, contours, true, color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Polygon Drawing", image);
	cv::waitKey(0);

	return 0;
}
