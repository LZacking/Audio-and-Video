#include <opencv2/opencv.hpp>

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// ����ֱ�ߵ������յ�����
	cv::Point start(100, 100);
	cv::Point end(500, 300);

	// ָ��ֱ�ߵ���ɫ��BGR��ʽ�����߿�
	cv::Scalar color(0, 0, 255); // ��ɫ
	int thickness = 2;

	// ��ͼ���ϻ���ֱ��
	cv::line(image, start, end, color, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Line Drawing", image);
	cv::waitKey(0);

	return 0;
}
