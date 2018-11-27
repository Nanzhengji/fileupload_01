package com.nz.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileuploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到上传文件的保存目录，将上传的文件放在webRoot目录下
        String path = this.getServletContext().getRealPath("/upload");
        File file = new File(path);
        //判断上传文件的保存目录是否存在
        if(!file.exists() && !file.isDirectory()){
             System.out.println(path + "目录不存在，需要创建！");
             //创建目录
             file.mkdir();
        }
        //消息提示
        String message = "";
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1.创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2.创建一个文件上传解析器
             ServletFileUpload upload = new ServletFileUpload(factory);
             //解决中文乱码
             upload.setHeaderEncoding("UTF-8");
             //3.判断提交的数据普通表单的数据还是带文件上传的表单
             if(!upload.isMultipartContent(request)){
                 //如果是表单数据普通表单,则按照传统方式获取数据
                 return ;
             }
             //4.使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
             List<FileItem> list = upload.parseRequest(request);
             for(FileItem item : list){
                 //如果fileItem中封装的是普通输入项的数据
                 if(item.isFormField()){
                     //获取字段名字
                     String name = item.getFieldName();
                     //解决普通输入项中中文乱码问题
                     String value = item.getString("UTF-8");
                     System.out.println(name + " = " + value);
                 }else{
                     //获得上传的文件名称
                     String filename = item.getName();
                     System.out.println(filename);
                     if(filename == null || filename.trim().equals(" ")){
                         continue;
                 }
                 //获取item中的上传文件的输入流
                 InputStream in = item.getInputStream();
                 //创建一个文件输入流
                 FileOutputStream out = new FileOutputStream(path + "\\" + filename);
                 //创建一个缓冲区
                 byte buffer[] = new byte[1024];
                 //判断输入流中的数据是否已经读取完毕的标志位
                 int len = 0;
                 //循环将输入流读入到缓冲区当中，（len = in.read(buffer)>0）就表示in里面还有数据存在
                 while((len = in.read(buffer)) > 0){
                     //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录（path+"\\"+filename）当中
                     out.write(buffer, 0, len);
                 }
                 //关闭输入流
                 in.close();
                 //关闭输出流
                 out.close();
                 //删除处理文件上传生成的临时文件
                 item.delete();
                 message = "文件上传成功!";
                 }
             }
        }catch(Exception e){
            message = "文件上传失败！";
            e.printStackTrace();
        }
        request.setAttribute("message", message);
        request.getRequestDispatcher("fileUploadResult.jsp").forward(request, response);
    }
}
