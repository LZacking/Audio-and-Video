#include <opencv2/opencv.hpp>

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// ������Բ�Ĳ���
	cv::Point center(300, 200); // ��Բ����������
	cv::Size axes(100, 50);    // ����Ͷ���ĳ���
	double angle = 30.0;       // ��Բ����ת�Ƕ�
	double startAngle = 0.0;   // ��ʼ�Ƕ�
	double endAngle = 360.0;   // ��ֹ�Ƕ�
	cv::Scalar color(0, 0, 255); // ��ɫ
	int thickness = 2;

	// ��ͼ���ϻ�����Բ
	cv::ellipse(image, center, axes, angle, startAngle, endAngle, color, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Ellipse Drawing", image);
	cv::waitKey(0);

	return 0;
}
