package site.greentable.service;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dao.ProductDAO;
import site.greentable.dao.ProductDAOImpl;
import site.greentable.dto.Product;
import site.greentable.dto.ProductDetail;
import site.greentable.dto.ProductImage;

public class ProductServiceImpl implements ProductService {
    private ProductDAO productDAO = new ProductDAOImpl();
    private static final int PAGE_SIZE = 5; // 한 페이지당 상품 수

    @Override
    public List<Product> getAllProducts(int pageNo) throws SQLException {
        return productDAO.selectAll(pageNo);
    }

    @Override
    public List<Product> getProductsByCategory(String category, int pageNo) throws SQLException {
        return productDAO.selectByCategory(category, pageNo);
    }

    @Override
    public List<Product> getBestProducts(int pageNo) throws SQLException {
        return productDAO.selectBestProducts(pageNo);
    }

    @Override
    public List<Product> getRegularProducts(int pageNo) throws SQLException {
        return productDAO.selectRegularProducts(pageNo);
    }

    @Override
    public List<Product> getLunchboxProducts(int pageNo) throws SQLException {
        return productDAO.selectLunchboxProducts(pageNo);
    }

    @Override
    public List<Product> getSaladProducts(int pageNo) throws SQLException {
        return productDAO.selectSaladProducts(pageNo);
    }

    @Override
    public Product getProductDetail(int productId) throws SQLException {
        return productDAO.selectProductDetail(productId);
    }

    @Override
    public int registerProduct(Product product, ProductDetail detail, List<ProductImage> images) throws SQLException {
        // 1. 상품 기본 정보 등록
        int productId = productDAO.insertProduct(product);
        if (productId <= 0) {
            return 0; // 상품 등록 실패
        }

        // 2. 상품 상세 정보 등록
        detail.setProductId(productId);
        int detailResult = productDAO.insertProductDetail(detail);
        if (detailResult <= 0) {
            // 롤백을 위해 등록된 상품 삭제
            productDAO.deleteProduct(productId);
            return 0;
        }

        // 3. 상품 이미지 등록
        for (ProductImage image : images) {
            image.setProductId(productId);
            int imageResult = productDAO.insertProductImage(image);
            if (imageResult <= 0) {
                // 롤백
                productDAO.deleteProductImages(productId);
                productDAO.deleteProduct(productId);
                return 0;
            }
        }

        return productId;
    }

    @Override
    public int updateProduct(Product product, ProductDetail detail) throws SQLException {
        int productResult = productDAO.updateProduct(product);
        if (productResult <= 0) {
            return 0;
        }

        int detailResult = productDAO.updateProductDetail(detail);
        if (detailResult <= 0) {
            return 0;
        }

        return productResult;
    }

    @Override
    public int deleteProduct(int productId) throws SQLException {
        // 먼저 연결된 이미지 삭제
        productDAO.deleteProductImages(productId);

        // 그 다음 상품 삭제
        return productDAO.deleteProduct(productId);
    }

    @Override
    public int getTotalPages() throws SQLException {
        int totalProducts = productDAO.getTotalProductCount();
        return (totalProducts + PAGE_SIZE - 1) / PAGE_SIZE; // 페이지 수 계산
    }

    @Override
    public int getCategoryTotalPages(String category) throws SQLException {
        int totalProducts = productDAO.getCategoryProductCount(category);
        return (totalProducts + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public int getBestTotalPages() throws SQLException {
        int totalProducts = productDAO.getBestProductCount();
        return (totalProducts + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public int getRegularTotalPages() throws SQLException {
        int totalProducts = productDAO.getRegularProductCount();
        return (totalProducts + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public int getLunchboxTotalPages() throws SQLException {
        int totalProducts = productDAO.getLunchboxProductCount();
        return (totalProducts + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public int getSaladTotalPages() throws SQLException {
        int totalProducts = productDAO.getSaladProductCount();
        return (totalProducts + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public ProductDetail getProductDetailInfo(int productId) throws SQLException {
        ProductDetail detail = null;
        Product product = productDAO.selectProductDetail(productId);
        if (product != null && product.getProductDetail() != null) {
            detail = product.getProductDetail();
        }
        return detail;
    }

    @Override
    public List<ProductImage> getProductImages(int productId) throws SQLException {
        return productDAO.selectProductImages(productId);
    }

    @Override
    public int updateProduct(Product product, ProductDetail detail, List<ProductImage> images, boolean updateImages)
            throws SQLException {
        // 1. 상품 기본 정보 업데이트
        int productResult = productDAO.updateProduct(product);
        if (productResult <= 0) {
            return 0;
        }

        // 2. 상품 상세 정보 업데이트
        int detailResult = productDAO.updateProductDetail(detail);
        if (detailResult <= 0) {
            return 0;
        }

        // 3. 이미지 업데이트가 필요한 경우
        if (updateImages && images != null && !images.isEmpty()) {
            // 기존 이미지 삭제
            productDAO.deleteProductImages(product.getProductId());

            // 새 이미지 등록
            for (ProductImage image : images) {
                image.setProductId(product.getProductId());
                int imageResult = productDAO.insertProductImage(image);
                if (imageResult <= 0) {
                    // 일부 이미지 등록 실패 시에도 진행 (롤백하지 않음)
                    continue;
                }
            }
        }

        return productResult;
    }

    @Override
    public int resetProductIdSequence() throws SQLException {
        return productDAO.resetProductAutoIncrement();
    }

    @Override
    public int getTotalProductCount() throws SQLException {
        return productDAO.getTotalProductCount();
    }
    
    @Override
    public List<Product> getRecommendedProducts(int productId, int limit) throws SQLException {
        return productDAO.selectRecommendedProducts(productId, limit);
    }
}