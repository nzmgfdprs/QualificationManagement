package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.QualificationDAO;
import dto.Login;

/**
 * Servlet implementation class History
 */
@WebServlet("/QualiHistory")
public class QualiHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QualiHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//formに入力された値を取得
		request.setCharacterEncoding("UTF-8");

		if(Top.flag){

			String myoji = request.getParameter("myoji");
			String mei = request.getParameter("name");
			String fullName = myoji.concat(mei);
			String pass = request.getParameter("pass");
			Login user = QualificationDAO.login(fullName);

			if(fullName.equals(user.getStuName()) && pass.equals(user.getStuPass())){

				//HttpSessionインスタンスの取得
				HttpSession session = request.getSession(false);

				//セッションスコープにインスタンスを保存
				session.setAttribute("user", user);

				Top.flag = false;

				if(user.getAuthorithy() == 0){
					session = request.getSession(true);

					String view = "/WEB-INF/view/history.jsp";
					RequestDispatcher dispatcher = request.getRequestDispatcher(view);
					dispatcher.forward(request, response);
				} else {
					response.sendRedirect("/QualificationManagement/ManagerTop");
				}

			} else {
				//エラーアラートの表示
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter printWriter = response.getWriter();
				printWriter.println("<script>");
				printWriter.println("alert('氏名またはパスワードに誤りがあります。');");		//アラート表示
				printWriter.println("history.go(-1)");											//前のページに戻るスクリプト
				printWriter.println("</script>");
			}


		} else {			//flag = falseの場合

			String view = "/WEB-INF/view/history.jsp";
			RequestDispatcher dispatcher = request.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		}
	}

}
