package cc.zzr;


import cc.zzr.bean.Data;
import cc.zzr.bean.Handler;
import cc.zzr.bean.Param;
import cc.zzr.bean.View;
import cc.zzr.helper.*;
import cc.zzr.util.JsonUtil;
import cc.zzr.util.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet(value = {"/*"}, loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    public void init(ServletConfig servletConfig) {
        HelperLoader.init();
        ServletContext servletContext = servletConfig.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(new String[]{ConfigHelper.getAppJspPath() + "*"});
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(new String[]{ConfigHelper.getAppAsset() + "*"});
        UploadHelper.init(servletContext);
    }

    public void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletHelper.init(req, resp);
        try {
            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();

            if (requestPath.equals("/favicon.ico")) {
                return;
            }
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                Class controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                Param param;
                if (UploadHelper.isMultipart(req))
                    param = UploadHelper.createParam(req);
                else {
                    param = RequestHelper.createParam(req);
                }

                Method actionMethod = handler.getActionMethod();
                Object result;
                if (param.isEmpty())
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, new Object[0]);
                else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, new Object[]{param});
                }
                if ((result instanceof View)) {
                    View view = (View) result;
                    handleViewResult(view, req, resp);
                } else if ((result instanceof Data)) {
                    Data data = (Data) result;
                    handleDataResult(data, resp);
                }
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (path.startsWith("/")) {
            response.sendRedirect(request.getContextPath() + path);
        } else {
            Map<String, Object> model = view.getModel();
            for (Map.Entry<String, Object> entry : model.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
        }
    }

    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
