package site.greentable.dao;

import site.greentable.dto.QnaDTO;

import java.util.List;

public interface QnaDAO {
    void insertQna(QnaDTO qna) throws Exception;
    List<QnaDTO> getProductQnas(int productId, int offset, int limit) throws Exception;
    List<QnaDTO> getUserQnas(int userId) throws Exception;
    List<QnaDTO> getUserQnasWithFilter(int userId, int period, String status) throws Exception;
    QnaDTO getQna(int qnaId) throws Exception;
    void updateQna(QnaDTO qna) throws Exception;
    void deleteQna(int qnaId, int userId) throws Exception;
    int getQnaCount(int productId) throws Exception;
    List<QnaDTO> getProductList(int userId) throws Exception;
}