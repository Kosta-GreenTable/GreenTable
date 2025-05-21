package site.greentable.service;

import site.greentable.dto.QnaDTO;

import java.util.List;

public interface QnaService {
    void writeQna(QnaDTO qna) throws Exception;
    List<QnaDTO> getProductQnas(int productId, int page) throws Exception;
    List<QnaDTO> getUserQnas(int userId) throws Exception;
    List<QnaDTO> getUserQnasWithFilter(int userId, int period, String status) throws Exception;
    QnaDTO getQna(int qnaId) throws Exception;
    void deleteQna(int qnaId, int userId) throws Exception;
    List<QnaDTO> getProductList(int userId) throws Exception;
	void updateQna(QnaDTO qna) throws Exception;
}