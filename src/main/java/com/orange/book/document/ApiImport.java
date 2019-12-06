package com.orange.book.document;

import java.util.Arrays;

public class ApiImport {
    public static void main(String[] args) throws Exception{
        ReadExcel readExcel = new ReadExcel();
        readExcel.readExcel("D:\\01.xlsx");
        RowReader rowrReader = new RowReader();
        //BigExcelReader reader = new BigExcelReader("D:\\\\360MoveData\\\\Users\\\\hundsun\\\\Desktop\\\\华夏银行中间业务云平台公共服务接口文档V2.0.0.0.xlsx");
       //// reader.setRowReader(rowrReader);
       // reader.process();
        //List<Map<String,String>> list = rowrReader.getList();
    }

        /*public static void main(String[] args) throws Exception{
            String filepath = "D:\\\\360MoveData\\\\Users\\\\hundsun\\\\Desktop\\\\华夏银行中间业务云平台公共服务接口文档V2.0.0.0.xlsx";
            BigExcelReader reader = new BigExcelReader(filepath) {
                @Override
                protected void outputRow(String[] datas, int[] rowTypes, int rowIndex) {
                    // 此处输出每一行的数据
                    System.out.println(Arrays.toString(datas));
                }
            };
            // 执行解析
            reader.parse();
        }*/
}
