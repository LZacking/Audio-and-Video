#include <opencv2/opencv.hpp>

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// �����ı�������ʹ�С
	int fontFace = cv::FONT_HERSHEY_SIMPLEX; // ��������
	double fontScale = 2.0; // �����С
	int thickness = 3; // �ı��߿�

	// �����ı���λ�ú�����
	cv::Point textPosition(100, 200); // �ı���ʼλ��
	std::string text = "Hello, OpenCV!";

	// �����ı�����ɫ
	cv::Scalar textColor(0, 0, 255); // ��ɫ

	// ʹ��ָ�������塢��С����ɫ�Ȳ��������ı�
	cv::putText(image, text, textPosition, fontFace, fontScale, textColor, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Text Drawing", image);
	cv::waitKey(0);

	return 0;
}
