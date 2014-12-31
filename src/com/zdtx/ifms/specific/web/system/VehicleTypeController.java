package com.zdtx.ifms.specific.web.system;

import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.system.VehType;
import com.zdtx.ifms.specific.service.system.VehicleTypeManager;

/**
 * 车辆类型
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@InterceptorRefs({ @InterceptorRef("restDefaultStack") })
@Result(name = "img", type = "stream", params = { "contentType", "image/jpeg","inputName", "image" })
public class VehicleTypeController extends URLSupport<VehType> {
	private static final String IMAGE_PATH = System.getProperty("catalina.home") + "/upload_imeges";// 图片保存路径
	private static final long serialVersionUID = 1L;
	@Autowired
	private VehicleTypeManager typeMgr;
	private VehType vehtype;
	
	private File iefile;
	private ByteArrayInputStream image;
	 private List<KeyAndValue> coms=new ArrayList<KeyAndValue>();
	public String index() {
		page = typeMgr.getBatch(page,Utils.keysToArray(baseMgr.getComByAuthority(getCurrentUser().getUserID())));
		return "index";
	}

	public String add() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		return "add";
	}

	public String edit() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		vehtype = baseMgr.get(VehType.class, id);
		return "add";
	}
	public String show() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		vehtype = baseMgr.get(VehType.class, id);
		return "show";
	}

	public String save() {
		try {
			
			if(vehtype.getComid()!=null){
				Org o=baseMgr.get(Org.class, vehtype.getComid());
				if(o!=null&&o.getOrgName()!=null){
					vehtype.setComname(o.getOrgName());
				}
			}
			vehtype.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			vehtype.setCreater(getCurrentUser().getUserName());
			baseMgr.save(vehtype);
			highlight = vehtype.getTypeid();
			// 删除旧的图片，在上传的时候不能删除，所以在这里做删除了
//			typeMgr.deleteOldImages(IMAGE_PATH);
			Utils.renderHtml(Constants.SUCCESS[0] + Utils.getBasePath() + "system/vehicle-type?editble=1&highlight=" + highlight + Constants.SUCCESS[1]);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.renderHtml(Constants.ERROR);
		}
		return null;
	}

	public String check() {
		String name = Utils.getParameter("type");
		Utils.renderText(typeMgr.checkRepeat(name,id));
		return null;
	}

	public String delete() {
		vehtype = baseMgr.get(VehType.class, id);
		vehtype.setIsdelete("T");
		baseMgr.save(vehtype);
		return index();
	}

	public String upload() throws IOException {
		InputStream input = null;
		FileOutputStream out = null;
		String fileName = Utils.getRequest().getParameter("qqfile"); // 文件名
		try {
			// 做IE判断,用于获取文件流
			input = (null != iefile) ? new FileInputStream(iefile) : Utils.getRequest().getInputStream();
			// 获取文件名
			fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8"); // url中文乱码处理
			// 文件的保存路径
			File path = new File(IMAGE_PATH);
			// 如果不存在，则创建
			if (!path.exists()) {
				path.mkdir();
			}
			// 为了防止文件重名，这里的重新命名(时间+______+原文件名)
			// 文件原名弃用，否则会出现中文文件名的图片无法正常上传，保存
			String newName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS").format(new Date()) + "_img" + fileName.substring(fileName.indexOf("."), fileName.length());
			// 根据新文件名创建指向新文件的输出流
			out = new FileOutputStream(new File(IMAGE_PATH + "/" + newName));
			// 用这个IOUtils写入文件
			IOUtils.copy(input, out);
			// 将文件名传到前台
			Utils.renderHtml("{name : \"" + newName + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 页面即时显示照片
	 * 
	 * @return
	 */
	public String showImg() {
		String name = "";
		File img = null;
		InputStream is = null;
		// 读取请求的文件名
		if (null != Utils.getParameter("name")) {
			name = Utils.getParameter("name");
		} else {
			name = (String) Utils.getSession().getAttribute("upload_img");
		}
		try {
			// 图片文件赋值
			img = new File(IMAGE_PATH + "/" + name);
			// 创建文件的Input流
			is = new FileInputStream(img);
			// 将图片读进来
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			// 将字节流附于imgCode
			image = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			image = getNoImage();// 得不到图片的话，用下面的方法刷了一个[no]回到前台
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "img";
	}

	/**
	 * 看这个车辆类型在不在用着
	 * 
	 * @return
	 */
	public String checkUsed() {
		Utils.renderText(typeMgr.checkTypeUsed(Long.valueOf(Utils.getParameter("typeid"))).toString());
		return null;
	}

	@Override
	public VehType getModel() {
		return null;
	}

	public VehType getVehtype() {
		return vehtype;
	}

	public void setVehtype(VehType vehtype) {
		this.vehtype = vehtype;
	}

	public File getIefile() {
		return iefile;
	}

	public void setIefile(File iefile) {
		this.iefile = iefile;
	}


	public ByteArrayInputStream getImage() {
		return image;
	}

	public void setImage(ByteArrayInputStream image) {
		this.image = image;
	}

	public List<KeyAndValue> getComs() {
		return coms;
	}

	public void setComs(List<KeyAndValue> coms) {
		this.coms = coms;
	}

	/**
	 * 找不到图片的用这玩应刷过去一个，这个方法和验证码一样
	 * 
	 * @return
	 */
	private ByteArrayInputStream getNoImage() {
		ByteArrayInputStream in = null;
		try {
			BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
			Graphics graph = image.getGraphics();// 图形上下文
			graph.setColor(Color.WHITE);
			graph.fillRect(0, 0, 20, 20);
			graph.setFont(new Font(Font.DIALOG, Font.LAYOUT_NO_LIMIT_CONTEXT, 14)); // 设定字体
			graph.setColor(Color.BLACK);
			graph.drawString("no", 1, 15);
			graph.dispose();// 图象生效

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);
				ImageIO.write(image, "JPEG", ImageIO.createImageOutputStream(out));
				imageOut.close();
				in = new ByteArrayInputStream(out.toByteArray());
			} catch (Exception e) {
				System.err.println("Failed to create pictures" + e.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
}
