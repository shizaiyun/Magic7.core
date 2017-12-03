package org.magic7.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class ConvertMhtToHtml {
	private static final Pattern pattern = Pattern.compile("\u0000[^\\\"\']{1,}(\\\"|\\')");
	private static final Pattern base64Pattern = Pattern.compile("(\u0000)[^\u0001]{1,}(\u0001)");//用于处理Base64字符串
	private static final Pattern base64CharSetPattern = Pattern.compile("(\u0000)[^\\?]{1,}(\\?)");//用于处理Base64字符串
	private static final Pattern base64ContentPattern = Pattern.compile("(\\?)[^\\?]{1,}(\u0001)");//用于处理Base64字符串
	private static final Pattern linkPattern = Pattern.compile("(\\<link){1,1}[^\\>]{1,}[\\>]{1,1}", Pattern.CASE_INSENSITIVE);
	private static final Pattern hrefPattern = Pattern.compile("(href=(\\\"|\'))[^\\\"\']*(\\\"|\')", Pattern.CASE_INSENSITIVE);

	/**
	 * 将 mht文件转换成 html文件
	 * 
	 * @param s_SrcMht
	 * @param s_DescHtml
	 */
	public static String mht2html(String s_SrcMht, String s_DescHtml,String fileName,String address) {
		try {
			System.out.println("s_SrcMht:"+s_SrcMht);
			System.out.println("s_DescHtml:"+s_DescHtml);
			System.out.println("fileName:"+fileName);
			String destFilePath = ("\u0000"+s_DescHtml.replaceAll(" ", "")).replaceAll("\\\\", "\u0001").replaceAll("\u0000[a-zA-Z0-9\u4e00-\u9fa5\\:^\u0001]*\u0001", "")
					.replaceAll("\u0001", "").replaceAll("\u0000", "")+".files";
			System.out.println("destFilePath:"+destFilePath);
			
			InputStream fis = new FileInputStream(s_SrcMht);
			Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
			MimeMessage msg = new MimeMessage(mailSession, fis);
			Object content = msg.getContent();
			List<Integer> textPart = new ArrayList<Integer>();
			List<Integer> binaryPart = new ArrayList<Integer>();
			if(content instanceof String) {
				SaveHtml(content.toString(), s_DescHtml, "utf-8");
				return content.toString();
			}
			if (content instanceof Multipart) {
				MimeMultipart mp = (MimeMultipart) content;

				String strEncodng = null;
				for (int i = 0; i < mp.getCount(); i++) {
					MimeBodyPart bp1 = (MimeBodyPart) mp.getBodyPart(i);
					strEncodng = getEncoding(bp1);
					if(strEncodng!=null&&!"".equals(strEncodng)) {
						textPart.add(i);
					} else
						binaryPart.add(i);
				}
				StringBuilder buffer = new StringBuilder();
				for(Integer textIndex:textPart) {
					MimeBodyPart bp1 = (MimeBodyPart) mp.getBodyPart(textIndex);
					strEncodng = getEncoding(bp1);
					String body = getHtmlText(bp1,address+"\\"+File.separator+"uploadFile"+"\\"+File.separator+fileName+".files", strEncodng);
					buffer.append(body);
				}

				File parent = null;
				if (mp.getCount() > 1) {
					parent = new File(new File(s_DescHtml).getAbsolutePath() + ".files");
					parent.mkdirs();
					if (!parent.exists()) {
						return "";
					}
				}
				String strText = buffer.toString();
				for(Integer binaryIndex:binaryPart) {
					MimeBodyPart bp = (MimeBodyPart) mp.getBodyPart(binaryIndex);
					strEncodng = getEncoding(bp);
					String strUrl = getResourcesUrl(bp);
					if (strUrl == null || strUrl.length() == 0)
						continue;


					String FilePath = parent.getAbsolutePath() + File.separator + getName(strUrl,binaryIndex);
					File resources = new File(FilePath);
					String urlRoot = getUrlRoot(getLocation(bp));
					if (SaveResourcesFile(resources, bp.getInputStream(),urlRoot)) {
						strText = strText.replace(strUrl, resources.getAbsolutePath());
					}
				}

				MimeBodyPart bp = (MimeBodyPart) mp.getBodyPart(textPart.get(0));
				strEncodng = getEncoding(bp);
				SaveHtml(strText, s_DescHtml, strEncodng);
				return strText;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getName(String strName, int ID) {
		char separator1 = '/';
		char separator2 = '\\';
		strName = strName.replaceAll("\r\n", "");

		if (strName.lastIndexOf(separator1) >= 0) {
			String name = strName.substring(strName.lastIndexOf(separator1) + 1);
			return name;
		}
		if (strName.lastIndexOf(separator2) >= 0) {
			String name = strName.substring(strName.lastIndexOf(separator2) + 1);
			return name;
		}
		return "";
	}
	public static boolean SaveHtml(String s_HtmlTxt, String s_HtmlPath, String s_Encode) {
		try {
			Writer out = null;
			out = new OutputStreamWriter(new FileOutputStream(s_HtmlPath, false), s_Encode);
			out.write(s_HtmlTxt);
			out.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	private static boolean SaveResourcesFile(File SrcFile, InputStream inputStream,String urlRoot) {
		if (SrcFile == null || inputStream == null) {
			return false;
		}
		String fileName = SrcFile.getName();
		if(fileName.contains(".css")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			try {
				String c = null;
				StringBuilder buffer  = new StringBuilder();
				while((c=reader.readLine())!=null) {
					buffer.append(c+"\n");
				}
				c = buffer.toString().replaceAll("url\\(\\.\\.", "url("+urlRoot);
				SaveHtml(c,SrcFile.getAbsolutePath(),"utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		} else {
			BufferedInputStream in = null;
			FileOutputStream fio = null;
			BufferedOutputStream osw = null;
			try {
				in = new BufferedInputStream(inputStream);
				fio = new FileOutputStream(SrcFile);
				osw = new BufferedOutputStream(new DataOutputStream(fio));
				int index = 0;
				byte[] a = new byte[1024];
				while ((index = in.read(a)) != -1) {
					osw.write(a, 0, index);
				}
				osw.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (osw != null)
						osw.close();
					if (fio != null)
						fio.close();
					if (in != null)
						in.close();
					if (inputStream != null)
						inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
	}
	@SuppressWarnings("rawtypes")
	private static String getResourcesUrl(MimeBodyPart bp) {
		if (bp == null) {
			return null;
		}
		try {
			Enumeration list = bp.getAllHeaders();
			while (list.hasMoreElements()) {
				javax.mail.Header head = (javax.mail.Header) list.nextElement();
				if (head.getName().compareTo("Content-Location") == 0) {
					String url = head.getValue();
					String base64Str = url;
					base64Str = base64Str.replaceAll("\\?\\=", "\u0001").replaceAll("\\=\\?", "\u0000");
					Matcher matcher = base64Pattern.matcher(base64Str);
					String realPath = "";
					while(matcher.find()) {
						String info = matcher.group();
						Matcher m = base64CharSetPattern.matcher(info);
						if(m.find()) {
							String charSet = m.group().replaceAll("\u0000|\\?", "");
							m = base64ContentPattern.matcher(info);
							if(m.find()) {
								String content = m.group().replaceAll("\u0001|\\?", "");
								realPath+=new String(Base64.decode(content),charSet);
							}
						}
					}
					if(!"".equals(realPath))
						url = realPath;
					return url;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	private static String getHtmlText(MimeBodyPart bp,String destFilePath  ,String strEncoding) {
		InputStream textStream = null;
		BufferedInputStream buff = null;
		BufferedReader br = null;
		Reader r = null;
		try {
			textStream = bp.getInputStream();
			buff = new BufferedInputStream(textStream);
			r = new InputStreamReader(buff, strEncoding);
			br = new BufferedReader(r);
			StringBuffer strHtml = new StringBuffer("");
			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				strHtml.append(strLine + "\n");
			}
			br.close();
			r.close();
			textStream.close();
			String content = strHtml.toString().replaceAll("\\\\", "\\/").replaceAll("src=(\"|')", "\u0000");
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()) {
				String imageName = matcher.group().replaceAll("\\\"|(\u0000[\\d\\D]*\\/)", "").replaceAll("\\?[\\D\\d]*", "");
				content = content.replaceAll(imageName, "\u0001"+imageName);
				content = content.replaceFirst("\u0000[^\u0000\u0001]{1,}\u0001", "src=\""+destFilePath+"\\/").replaceAll("\u0001", "");
			}
			content = content.replaceAll("\u0000|\u0001", "");
			content = content.replaceAll("\u0000|\u0001", "");
			matcher = linkPattern.matcher(content);
			while(matcher.find()) {
				String imageName = matcher.group();
				Matcher m = hrefPattern.matcher(imageName);
				if(m.find()) {
					String href=m.group();
					String clone = new String(href).replaceAll("(href=)[\\s\\S]{1,}(\\\\|\\/)", "").replaceAll("\\\"|\\'", "");
					content = content.replaceAll(href,"href=\\\""+destFilePath+"\\\\"+clone+"\\\"");
				}
			}
			content = content.replaceAll("type=text[\\s]*/[\\s]*css", "type=\"text/css\"");
			content = content.replaceAll("\u0000|\u0001", "");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (buff != null)
					buff.close();
				if (textStream != null)
					textStream.close();
			} catch (Exception e) {
			}
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	private static String getEncoding(MimeBodyPart bp) {
		if (bp == null) {
			return null;
		}
		try {
			Enumeration list = bp.getAllHeaders();
			while (list.hasMoreElements()) {
				javax.mail.Header head = (javax.mail.Header) list.nextElement();
				if (head.getName().equals("Content-Type") ) {
					String strType = head.getValue();
					if(strType.indexOf("text/css")>=0) {
						return null;
					}
					int pos = strType.indexOf("charset=");
					if (pos >= 0) {
						String strEncoding = strType.substring(pos + 8, strType.length());
						if (strEncoding.startsWith("\"") || strEncoding.startsWith("\'")) {
							strEncoding = strEncoding.substring(1, strEncoding.length());
						}
						if (strEncoding.endsWith("\"") || strEncoding.endsWith("\'")) {
							strEncoding = strEncoding.substring(0, strEncoding.length() - 1);
						}
						if (strEncoding.toLowerCase().compareTo("gb2312") == 0) {
							strEncoding = "gbk";
						}
						if(strEncoding==null||"".equals(strEncoding))
							return "UTF-8";
						return strEncoding;
					}
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String getUrlRoot(String path) {
		if(path==null)
			return "";
		String infos[] = path.split("\\/");
		if(infos.length>3) {
			return "http://"+infos[2];
		} else
			return "";
	}
	@SuppressWarnings("rawtypes")
	private static String getLocation(MimeBodyPart bp) {
		if (bp == null) {
			return null;
		}
		try {
			Enumeration list = bp.getAllHeaders();
			while (list.hasMoreElements()) {
				javax.mail.Header head = (javax.mail.Header) list.nextElement();
				if (head.getName().equals("Content-Location") ) {
					String strType = head.getValue();
					return strType;
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
