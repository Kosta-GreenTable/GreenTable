package site.greentable.service;

import site.greentable.dao.QnaDAO;
import site.greentable.dao.QnaDAOImpl;
import site.greentable.dto.QnaDTO;

import java.util.List;

public class QnaServiceImpl implements QnaService {
    private QnaDAO qnaDAO;

    public QnaServiceImpl() {
        this.qnaDAO = new QnaDAOImpl();
    }

    @Override
    public void writeQna(QnaDTO qna) throws Exception {
        qnaDAO.insertQna(qna);
    }

    @Override
    public List<QnaDTO> getProductQnas(int productId, int page) throws Exception {
        int limit = 10; // 페이지당 Q&A 수
        int offset = (page - 1) * limit;

        return qnaDAO.getProductQnas(productId, offset, limit);
    }

    @Override
    public List<QnaDTO> getUserQnas(int userId) throws Exception {
        return qnaDAO.getUserQnas(userId);
    }

    @Override
    public QnaDTO getQna(int qnaId) throws Exception {
        return qnaDAO.getQna(qnaId);
    }

    @Override
    public void updateQna(QnaDTO qna) throws Exception {
        qnaDAO.updateQna(qna);
    }

    @Override
    public void deleteQna(int qnaId, int userId) throws Exception {
        qnaDAO.deleteQna(qnaId, userId);
    }
    
    @Override
    public List<QnaDTO> getUserQnasWithFilter(int userId, int period, String status) throws Exception {
        return qnaDAO.getUserQnasWithFilter(userId, period, status);
    }

    @Override
    public List<QnaDTO> getProductList(int userId) throws Exception {
        return qnaDAO.getProductList(userId);
    }
}