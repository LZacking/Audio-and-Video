#include <opencv2/opencv.hpp>

int main() {
	// ����һ�����ڲ�ָ������
	cv::namedWindow("My Window", cv::WINDOW_AUTOSIZE);

	// �ȴ�һ��ʱ��
	cv::waitKey(2000); // �ȴ�2����

	// �޸Ĵ��ڴ�СΪ900x400����
	cv::resizeWindow("My Window", 900, 400);

	// �ȴ�һ��ʱ��
	cv::waitKey(2000); // �ȴ�2����

	// ɾ������
	cv::destroyWindow("My Window");

	// �ȴ�һ��ʱ��
	cv::waitKey(2000); // �ȴ�2����

	// ����һ���´��ڲ�ָ������
	cv::namedWindow("Another Window", cv::WINDOW_AUTOSIZE);

	// �ȴ�һ��ʱ��
	cv::waitKey(2000); // �ȴ�2����

	// ɾ�����д���
	cv::destroyAllWindows();

	return 0;
}
