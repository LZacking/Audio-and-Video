#include <opencv2/opencv.hpp>

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// ����Բ������
	cv::Point center(300, 200);

	// ����Բ�İ뾶
	int radius = 50;

	// ָ��Բ����ɫ��BGR��ʽ�����߿�
	cv::Scalar color(0, 0, 255); // ��ɫ
	int thickness = 2;

	// ��ͼ���ϻ���Բ
	cv::circle(image, center, radius, color, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Circle Drawing", image);
	cv::waitKey(0);

	return 0;
}
