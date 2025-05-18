<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>

<section class="error-section">
  <div class="error-container">
    <div class="error-icon">
      <i class="fas fa-exclamation-triangle"></i>
    </div>
    <h3 class="error-title">오류가 발생했습니다</h3>
    <p class="error-message">${errorMessage}</p>
    <button class="reload-btn" onclick="location.reload()">
      <i class="fas fa-sync-alt"></i> 새로고침
    </button>
  </div>
</section>
