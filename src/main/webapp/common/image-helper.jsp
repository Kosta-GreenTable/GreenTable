<%@ page import="site.greentable.util.ImageUtil" %>
<%!
    // JSP에서 사용할 수 있는 이미지 URL 생성 함수
    public static String getImageUrl(String imageName, String contextPath) {
        return ImageUtil.getImageUrl(imageName, contextPath);
    }
    
    public static String getReviewImageUrl(String imageName, String contextPath) {
        return ImageUtil.getReviewImageUrl(imageName, contextPath);
    }
%>
