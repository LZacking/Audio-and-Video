#include <opencv2/opencv.hpp>

int main() {
	// 创建一个窗口并指定标题
	cv::namedWindow("My Window", cv::WINDOW_AUTOSIZE);

	// 等待一段时间
	cv::waitKey(2000); // 等待2秒钟

	// 修改窗口大小为900x400像素
	cv::resizeWindow("My Window", 900, 400);

	// 等待一段时间
	cv::waitKey(2000); // 等待2秒钟

	// 删除窗口
	cv::destroyWindow("My Window");

	// 等待一段时间
	cv::waitKey(2000); // 等待2秒钟

	// 创建一个新窗口并指定标题
	cv::namedWindow("Another Window", cv::WINDOW_AUTOSIZE);

	// 等待一段时间
	cv::waitKey(2000); // 等待2秒钟

	// 删除所有窗口
	cv::destroyAllWindows();

	return 0;
}
