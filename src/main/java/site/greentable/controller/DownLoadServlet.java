package site.greentable.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.util.Env;

@WebServlet("/downLoad")
public class DownLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. 넘어오는 파일의 이름을 받기(실제 파일 이름,다운로드받을 파일 이름)
		String realFName = request.getParameter("realFName");
		String originalFName = request.getParameter("originalFName");
		
		// path traversal 공격 방어
		realFName = realFName.replace("/", "").replace("..", "").replace("\\", "");
		
		// 2. 저장폴더의 실제 경로를 얻어오기
		String saveDir = null;
		if ("developement".equals(Env.pr.getProperty("env"))) {
			saveDir = request.getServletContext().getRealPath("/save");
		} else if ("production".equals(Env.pr.getProperty("env"))) {
			File contextPath = new File(request.getServletContext().getRealPath("/"));
			File parentDir = contextPath.getParentFile();
			saveDir = new File(parentDir, "save").getAbsolutePath();
		}

		File file = new File(saveDir, realFName);

		// 부가적인 옵션!!!
		// 요청된 파일의 mimeType을 설정한다(문서의 형태설정)

		String mimeType = getServletContext().getMimeType(file.toString());

		if (mimeType == null) {
			response.setContentType("application/octet-stream");
		}

		System.out.println("mimeType = " + mimeType);

		// 브라우져 별 파일이름에대한 한글인코딩설정
		// 다운로드되는 파일이름을 파라미터로 받을것이므로 필요 없는 작업 
//		if (request.getHeader("user-agent").indexOf("Trident") == -1) {// IE가 아닌경우
//
//			fName = new String(file.getName().getBytes("UTF-8"), "8859_1");
//		} else {
//
//			fName = new String(file.getName().getBytes("euc-kr"), "8859_1");
//		}

		// 브라우져가 해석할수 있는 파일을 해석하지 않고 다운로드!!!
		// 파라미터로 전송되는 원래의 파일명으로 파일을 다운로드한다
		response.setHeader("Content-Disposition", "attachment;filename=\"" + originalFName + "\";");

		// 3. 폴더에서 파일이름에 해당하는 파일을 읽어서
		// 클라이언트 브라우져에서 다운로드(출력=쓰기)

		FileInputStream fi = new FileInputStream(file);
		ServletOutputStream so = response.getOutputStream();

		byte b[] = new byte[1024];

		int i = 0;
		while ((i = fi.read(b)) != -1) {
			so.write(b);
		}

		so.flush();
		fi.close();
		so.close();

	}

}
