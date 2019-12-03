package com.orange.book.document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * 类名称：BigExcelReader
 * 类描述：
 */
public class BigExcelReader {
	private XSSFReader xssfReader;
	//获取一行时最小数组长度
	private int currentRow = 0;
	private int sheetIndex = -1;
	private int thisColumnIndex = -1;
	// 日期标志
	private boolean dateFlag;
	// 数字标志
	private boolean numberFlag;
	private boolean isTElement;
	private RowReader rowReader;
	private List<String> rowlist = new ArrayList<String>();
	public void setRowReader(RowReader rowReader) {
		this.rowReader = rowReader;
	}

	/**
	 * 构造方法
	 */
	public BigExcelReader(String filename) throws Exception {
		if (StringUtils.isEmpty(filename)) {
			throw new Exception("文件名不能空");
		}
		OPCPackage pkg = OPCPackage.open(filename);
		init(pkg);

	}
	public BigExcelReader(InputStream is) throws Exception {
		if (null == is) {
			throw new Exception("文件不能空");
		}
		OPCPackage pkg = OPCPackage.open(is);
		init(pkg);
	}
	
	public BigExcelReader(MultipartFile file) throws Exception {
		if (null == file) {
			throw new Exception("文件不能空");
		}
		//OPCPackage pkg = OPCPackage.open(file);
		CommonsMultipartFile cf = (CommonsMultipartFile) file;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File f = fi.getStoreLocation();
		OPCPackage pkg = OPCPackage.open(f);
		init(pkg);
	}
	private void init(OPCPackage pkg) throws IOException, OpenXML4JException {
		xssfReader = new XSSFReader(pkg);
	}

	/**
	 * 获取sheet
	 * @throws Exception
	 */
	public void process() throws Exception {
		SharedStringsTable sst = xssfReader.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		Iterator<InputStream> it = xssfReader.getSheetsData();
		while (it.hasNext()) {
			sheetIndex++;
			InputStream sheet = it.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
		}
	}

	/**
	 * 加载sax 解析器
	 * 
	 * @param sst
	 * @return
	 * @throws SAXException
	 */
	private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new PagingHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private class PagingHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;
		private String index = null;

		private PagingHandler(SharedStringsTable sst) {
			this.sst = sst;
		}

		/**
		 * 开始元素
		 */
		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			if (name.equals("c")) {
				index = attributes.getValue("r");
				int firstDigit = -1;
				for (int c = 0; c < index.length(); ++c) {
					if (Character.isDigit(index.charAt(c))) {
						firstDigit = c;
						break;
					}
				}
				thisColumnIndex = nameToColumn(index.substring(0, firstDigit));
				
				// 判断是否是新的一行
				if (Pattern.compile("^A[0-9]+$").matcher(index).find()) {
					currentRow++;
				}
				String cellType = attributes.getValue("t");
				if (cellType != null && cellType.equals("s")) {
					nextIsString = true;
				} else {
					nextIsString = false;
				}
				// 日期格式
				String cellDateType = attributes.getValue("s");
				if ("1".equals(cellDateType)) {
					dateFlag = true;
				} else {
					dateFlag = false;
				}
				String cellNumberType = attributes.getValue("s");
				if ("2".equals(cellNumberType)) {
					numberFlag = true;
				} else {
					numberFlag = false;
				}
			}
			// 当元素为t时
			if ("t".equals(name)) {
				isTElement = true;
			} else {
				isTElement = false;
			}
			lastContents = "";
		}

		/**
		 * 获取value
		 */
		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}
			// t元素也包含字符串
			if (isTElement) {
				String value = lastContents.trim();
				rowlist.add(thisColumnIndex,value);
				isTElement = false;
				// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
				// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
			} else if ("v".equals(name)) {
				String value = lastContents.trim();
				value = value.equals("") ? "" : value;
			/*	// 日期格式处理
				if (dateFlag) {
					try {
						Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						value = dateFormat.format(date);
					} catch (NumberFormatException e) {
					}
				}
				// 数字类型处理
				if (numberFlag) {
					try {
						BigDecimal bd = new BigDecimal(value);
						value = bd.setScale(3, BigDecimal.ROUND_UP).toString();
					} catch (Exception e) {
					}
				}*/
				//rowlist.add(thisColumnIndex,value);
				
			} else {
				if (name.equals("row")) {
					if (rowlist.size() > 0) {
						System.out.println(rowlist.toString());
						rowReader.getRows(sheetIndex, currentRow, rowlist);
						rowlist.clear();
					}
				}
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}

	}
	private int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
	/*	RowReader rowrReader = new RowReader();
		BigExcelReader reader = new BigExcelReader("D:\\360MoveData\\Users\\hundsun\\Desktop\\省行对私贷款漂白1.xlsx");
		reader.setRowReader(rowrReader);
		reader.process();
		List<Map<String,String>> list = rowrReader.getList();
		System.out.println(list.toString());
		long end = System.currentTimeMillis();*/
		//System.out.println((end - start) / 1000);
	}
}