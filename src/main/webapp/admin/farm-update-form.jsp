<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>그린테이블 관리자 - 농가 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="admin-container">
        
        <div class="admin-content">
            <h2>농가 정보 수정</h2>
            
            <form id="farmUpdateForm" action="${pageContext.request.contextPath}/front?key=farm&methodName=update" method="post">
                <input type="hidden" name="farmId" value="${farm.farmId}">
                
                <div class="form-group">
                    <label for="name">농가명 <span class="required">*</span></label>
                    <input type="text" id="name" name="name" value="${farm.name}" required class="form-control">
                </div>
                
                <div class="form-group">
                    <label for="description">설명</label>
                    <textarea id="description" name="description" class="form-control" rows="5">${farm.description}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="address">주소 <span class="required">*</span></label>
                    <input type="text" id="address" name="address" value="${farm.address}" required class="form-control">
                </div>
                
				<div class="form-group">
				  <label for="category">카테고리</label>
				  <select id="category" name="category" class="form-control">
				    <option value="일반" ${farm.category eq '일반' ? 'selected' : ''}>일반</option>
				    <option value="채소" ${farm.category eq '채소' ? 'selected' : ''}>채소</option>
				    <option value="과일" ${farm.category eq '과일' ? 'selected' : ''}>과일</option>
				    <option value="곡물" ${farm.category eq '곡물' ? 'selected' : ''}>곡물</option>
				    <option value="축산물" ${farm.category eq '축산물' ? 'selected' : ''}>축산물</option>
				    <option value="유기농" ${farm.category eq '유기농' ? 'selected' : ''}>유기농</option>
				  </select>
				</div>
                
                <div class="form-group">
                    <label for="farmImg">이미지 URL</label>
                    <input type="text" id="farmImg" name="farmImg" value="${farm.farmImg}" class="form-control">
                    <small class="form-text text-muted">이미지 URL을 입력하세요. (예: /images/farms/farm1.jpg)</small>
                </div>
                
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="latitude">위도</label>
                        <input type="number" id="latitude" name="latitude" value="${farm.latitude}" step="0.000001" class="form-control">
                    </div>
                    <div class="form-group col-md-6">
                        <label for="longitude">경도</label>
                        <input type="number" id="longitude" name="longitude" value="${farm.longitude}" step="0.000001" class="form-control">
                    </div>
                </div>
                
                <div class="form-group">
    				<label for="contractStatus">계약 상태</label>
    					<select id="contractStatus" name="contractStatus" class="form-control">
				        <option value="활성" ${farm.contractStatus eq '활성' ? 'selected' : ''}>활성</option>
				        <option value="종료" ${farm.contractStatus eq '종료' ? 'selected' : ''}>종료</option>
				    </select>
				</div>
                
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">수정 저장</button>
                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList" class="btn btn-secondary">취소</a>
                </div>
            </form>
        </div>
        
    </div>
    
    <script>
        $(document).ready(function() {
            $('#farmUpdateForm').on('submit', function(e) {
                // 필수 필드 유효성 검사
                var name = $('#name').val().trim();
                var address = $('#address').val().trim();
                
                if (!name) {
                    alert('농가명은 필수 입력 항목입니다.');
                    $('#name').focus();
                    e.preventDefault();
                    return false;
                }
                
                if (!address) {
                    alert('주소는 필수 입력 항목입니다.');
                    $('#address').focus();
                    e.preventDefault();
                    return false;
                }
            });
        });
    </script>
</body>
</html>
