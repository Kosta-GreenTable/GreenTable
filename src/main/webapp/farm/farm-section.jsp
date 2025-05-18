<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<section class="category-section-preview" id="farm-preview">
  <div class="section-header">
    <h2 class="section-title">농가 소개</h2>
    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=list" class="view-more-btn">더 보기</a>
  </div>
  
  <c:choose>
    <c:when test="${empty farmList}">
      <div class="no-products-message">등록된 농가가 없습니다.</div>
    </c:when>
    <c:otherwise>
      <div class="farm-slider">
        <button class="arrow arrow-left"><i class="fas fa-chevron-left"></i></button>
        <div class="farm-container">
          <c:forEach var="farm" items="${farmList}">
            <div class="farm-card">
              <a href="${pageContext.request.contextPath}/front?key=farm&methodName=detail&farmId=${farm.farmId}" class="farm-card-link">
                <div class="farm-img">
                  <img 
                    src="${pageContext.request.contextPath}/assets/images/farms/${farm.farmImg}" 
                    alt="${farm.name}" 
                    onerror="this.onerror=null; this.src='https://picsum.photos/seed/farm${farm.farmId}/300/250';"
                  >
                </div>
                <div class="farm-info">
                  <h3 class="farm-name">${farm.name}</h3>
                  <p class="farm-desc">${empty farm.description ? '농가 설명이 없습니다.' : farm.description}</p>
                  <p class="farm-location"><i class="fas fa-map-marker-alt"></i> ${empty farm.address ? '주소 정보 없음' : farm.address}</p>
                </div>
              </a>
            </div>
          </c:forEach>
        </div>
        <button class="arrow arrow-right"><i class="fas fa-chevron-right"></i></button>
      </div>
    </c:otherwise>
  </c:choose>
</section>