package cn.ezeyc.core.util;

import cn.ezeyc.core.annotation.Excel;
import cn.ezeyc.core.pojo.MyRuntimeException;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.noear.solon.core.handle.Context;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zewang
 */
public class ExcelUtil {


    /**
     * PC导出表格
     * @param redisUtil redis缓存
     * @param list  数据集合
     */
    public static void pcExportExcel(RedisUtil redisUtil,List list){
        pcExportExcel(redisUtil,list,null,null);
    }

    /**
     * PC导出表格
     * @param list 数据集合
     */
    public static void pcExportExcel(List list){
        pcExportExcel(null,list,null,null);
    }

    /**
     * PC导出表格
     * @param redisUtil  redis缓存
     * @param list 数据集合
     * @param fileName 文件名称
     */
    public static void pcExportExcelName(RedisUtil redisUtil,List list,String fileName){
        pcExportExcel(redisUtil,list,null,fileName);
    }
    /**
     * PC导出表格
     * @param list 数据集合
     * @param fileName 文件名称
     */
    public static void pcExportExcelName(List list,String fileName){
        pcExportExcel(null,list,null,fileName);
    }

    /**
     * PC导出表格
     * @param redisUtil redis缓存
     * @param list 数据集合
     * @param title 标题
     */
    public static void pcExportExcel(RedisUtil redisUtil,List list,String title){
        pcExportExcel(redisUtil,list,title,null);
    }
    /**
     * PC导出表格
     * @param list 数据集合
     * @param title 标题
     */
    public static void pcExportExcel(List list,String title){
        pcExportExcel(null,list,title,null);
    }

    /**
     * PC导出表格
     * @param redisUtil redis缓存
     * @param list 数据集合
     * @param title 标题
     * @param fileName 文件名称
     */
    public static void pcExportExcel(RedisUtil redisUtil,List list,String title,String fileName){
        export(list,null,title,fileName,true,redisUtil);
    }
    /**
     * PC导出表格
     * @param list 数据集合
     * @param title 标题
     * @param fileName 文件名称
     */
    public static void pcExportExcel(List list,String title,String fileName){
        export(list,null,title,fileName,true,null);
    }

    /**
     * 非PC导出表格
     * @param list 数据集合
     * @param path  文件路径
     */
    public static void exportExcel(List list,String path){
        exportExcel(null,list,path,null);
    }

    /**
     * 非pc导出表格
     * @param redisUtil redis缓存
     * @param list  数据集合
     * @param path 文件路径
     */
    public static void exportExcel(RedisUtil redisUtil,List list,String path){
        exportExcel(redisUtil,list,path,null);
    }

    /**
     * 非pc导出表格
     * @param list 数据集合
     * @param path 文件路径
     * @param title  标题
     */
    public static void exportExcel(List list,String path,String title){
        exportExcel(null,list,path,title,null);
    }
    /**
     * 非pc导出表格
     * @param list 数据集合
     * @param title 标题
     */
    public static void exportExcel(RedisUtil redisUtil,List list,String path,String title){
        exportExcel(redisUtil,list,path,title,null);
    }

    /**
     *
     * @param list 数据集合
     * @param path 文件路径
     * @param title 标题
     * @param fileName 文件名称
     */
    public static void exportExcel(List list,String path,String title,String fileName){
        export(list,path,title,fileName,false,null);
    }
    /**
     * 非PC导出表格  本地提供下载
     * @param list 数据集合
     * @param path 文件路径
     * @param title 标题
     * @param fileName 文件名称
     */
    public static void exportExcel(RedisUtil redisUtil,List list,String path,String title,String fileName){
        export(list,path,title,fileName,false,redisUtil);
    }
    /**
     * 导出表格
     * @param list 数据集合
     * @param title 标题
     * @param fileName 文件名称
     */
    private static void export(List list,String path,String title,String fileName,Boolean pc,RedisUtil redisUtil){
        Workbook work = new XSSFWorkbook();
        Sheet sheet = work.createSheet();
        Field[] field = ClassUtil.getAllFields(list.get(0).getClass(), Excel.class);
        //排序
        List<Field> fields = Arrays.stream(field).sorted((o1, o2) -> o1.getAnnotation(Excel.class).index() - o2.getAnnotation(Excel.class).index()).collect(Collectors.toList());
        Integer rowInt = 0;
        //设置标题
        if(title!=null&&!"".equals(title)){
            //合并标题
            CellRangeAddress cellAddresses = new CellRangeAddress(0, 0, 0, fields.size());
            sheet.addMergedRegion(cellAddresses);
            //创建大标题行
            Row titles = sheet.createRow(rowInt++);
            Cell cells = titles.createCell(0);
            cells.setCellValue(title);
//                    cells.setCellStyle(cellStyletitle);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellAddresses, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddresses, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddresses, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, cellAddresses, sheet);
        }

        if(fields.size()>0){
            //列名
            Row row = sheet.createRow(rowInt++);
            int c=0;
            for (Field f:fields) {
                Excel annotation = f.getAnnotation(Excel.class);
                if(annotation.type()==Excel.Type.ALL||annotation.type()==Excel.Type.EXPORT){
                    //设置Excel表头
                    Cell cell = row.createCell(c++);
                    cell.setCellValue(annotation.name());
                }
            }
        }
        //数据
        for (Object o : list) {
            Row row = sheet.createRow(rowInt++);
            int c=0;
            for (Field f:fields) {
                Excel annotation = f.getAnnotation(Excel.class);
                if(annotation.type()==Excel.Type.ALL||annotation.type()==Excel.Type.EXPORT){
                    //设置Excel表头
                    Cell cell = row.createCell(c++);
                    try {
                        f.setAccessible(true);
                        if(!"".equals(annotation.dateFormat())&&f.get(o)!=null
                                &&(f.getType()== LocalDateTime.class||f.getType()== LocalTime.class||f.getType()== LocalDate.class)){
                            cell.setCellValue(parseDate(f,f.getType(),f.get(o).toString(),annotation.dateFormat())) ;
                        }else if(!"".equals(annotation.dictType())&&redisUtil!=null){
                            Object obj = redisUtil.getByString(annotation.dictType());
                            if(obj!=null && obj instanceof JSONObject){
                                JSONArray vals= ((JSONObject) obj).getJSONArray("list");
                                if(vals!=null&&vals.size()>0){
                                    for(Object j:vals){
                                        if(j!=null && j instanceof JSONObject){
                                            if(((JSONObject) j).get("val").equals(f.get(o).toString())){
                                                cell.setCellValue(((JSONObject) j).get("label").toString());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(!"".equals(annotation.readConverterExp())){
                            String[] split = annotation.readConverterExp().split(",");
                            if(split.length>0){
                                for(String s: split){
                                    String[] v = s.split("=");
                                    if(v.length>0&&v[0].equals(f.get(o).toString())){
                                        cell.setCellValue(v[1]);
                                        break;
                                    }
                                }
                            }
                        }else if(!"".equals(annotation.suffix())){
                            //后缀
                            cell.setCellValue(f.get(o).toString()+annotation.suffix());
                        }
                        else if(f.get(o)!=null){
                            if(f.getType().isArray()){
                                if(f.getType().getSimpleName().contains("String")){
                                    cell.setCellValue(StringUtils.join(Arrays.asList((String[]) f.get(o)), ","));
                                }else if(f.getType().getSimpleName().contains("Long")){
                                    cell.setCellValue(StringUtils.join(Arrays.asList((Long[]) f.get(o)), ","));
                                }else if(f.getType().getSimpleName().contains("Double")){
                                    cell.setCellValue(StringUtils.join(Arrays.asList((Double[]) f.get(o)), ","));
                                }else if(f.getType().getSimpleName().contains("Float")){
                                    cell.setCellValue(StringUtils.join(Arrays.asList((Float[]) f.get(o)), ","));
                                }
                            }else {
                                cell.setCellValue(f.get(o).toString());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        //导出
        try {
            if(fileName==null||"".equals(fileName)){
                fileName="表格.xlsx";
            }
            if(pc){
                download(work,fileName);
            }else{
                write(work,fileName,path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static String parseDate(Field f,Class c ,String value,String pattern){
        if(value != null&&!"".equals(value)&&c==LocalDate.class){
            LocalDate localDate = setDate(f.getName(), "yyyy-MM-dd", value).toLocalDate();
            return localDate.format(DateTimeFormatter.ofPattern(pattern));
        }else if(value != null&&!"".equals(value)&&c==LocalDateTime.class){
            if(String.valueOf(value).contains("Z")){
                LocalDateTime localDateTime = setDate(f.getName(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", value);

                return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
            }else if(String.valueOf(value).contains("T")){
                DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime localDateTime = setDate(f.getName(), "yyyy-MM-dd'T'HH:mm:ss", dateTimeFormatter.format(LocalDateTime.parse(value.toString())));
                return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
            }else {
                LocalDateTime localDateTime = setDate(f.getName(), "yyyy-MM-dd HH:mm:ss", value);
                return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
            }
        }else if(value != null&&!"".equals(value)&&c==LocalTime.class){
            LocalDateTime localDateTime = setDate(f.getName(),"HH:mm:ss",value);
            return localDateTime.toLocalTime().format(DateTimeFormatter.ofPattern(pattern));
        }
        return  null;
    }
    private static LocalDateTime setDate( String field, String format,Object value){
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date utcDate = null;
        try {
            utcDate = sdf.parse(String.valueOf(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Instant instant = utcDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return  instant.atZone(zoneId).toLocalDateTime();
    }

    public static List importExcel(File file, Class entity,RedisUtil redisUtil) {
        List list=new ArrayList();
        try{
            InputStream is = new FileInputStream(file);
            Field[] fields = ClassUtil.getAllFields(entity,Excel.class);
            XSSFWorkbook book = new XSSFWorkbook(is);
            XSSFSheet sheet = book.getSheetAt(0);  //获取sheet
            int rows = sheet.getPhysicalNumberOfRows();
            Map<Integer,String> cell=new HashMap<>();
            for(int x=0;x<rows;x++){
                XSSFRow row = sheet.getRow(x);   //获取数据行
                //标题行
                if(x==0){
                    for(int y=0;y<row.getPhysicalNumberOfCells();y++){
                        for(Field f:fields){
                            Excel annotation = f.getAnnotation(Excel.class);
                            if((annotation.type()==Excel.Type.ALL||annotation.type()==Excel.Type.IMPORT)&&annotation.name().equals(row.getCell(y).getStringCellValue())){
                                cell.put(y,f.getName());
                                break;
                            }
                        }
                    }
                }else{
                    //数据
                    Object obj=entity.newInstance();
                    for(int y=0;y<row.getPhysicalNumberOfCells();y++){
                        for (Integer key : cell.keySet()) {
                            if(y==key){
                                for(Field f:fields){
                                    if(f.getName().equals(cell.get(key))){
                                        Excel annotation = f.getAnnotation(Excel.class);
                                        //字典项目
                                        if(!"".equals(annotation.dictType())&&redisUtil!=null){
                                            Object redis = redisUtil.getByString(annotation.dictType());
                                            if(redis!=null){
                                                JSONArray vals= ((JSONObject) redis).getJSONArray("list");
                                                if(vals!=null&&vals.size()>0){
                                                    for(Object j:vals){
                                                        if(j!=null){
                                                            if(((JSONObject) j).get("label").equals(row.getCell(y).getStringCellValue())){
                                                                reflect.setNormal(f,f.getType(),entity,((JSONObject) j).get("val"));
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        //转换
                                        }else if(!"".equals(annotation.readConverterExp())){
                                            String[] split = annotation.readConverterExp().split(",");
                                            if(split.length>0){
                                                for(String s: split){
                                                    String[] v = s.split("=");
                                                    if(v.length>0&&v[1].equals(row.getCell(y).getStringCellValue())){
                                                        reflect.setNormal(f,f.getType(),entity,v[0]);
                                                        break;
                                                    }
                                                }
                                            }
                                        }else if(!"".equals(annotation.suffix())){
                                            reflect.setNormal(f,f.getType(),entity,row.getCell(y).getStringCellValue().replace(annotation.suffix(),""));
                                            break;
                                        }else {
                                            reflect.setNormal(f,f.getType(),entity,row.getCell(y).getStringCellValue());
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    list.add(obj);
                }
            }
        }catch (Exception e){
           throw new MyRuntimeException(e.getMessage());
        }
        return list;
    }
    /**
     *
     * @param workbook
     * @param fileName
     * @throws IOException
     */
    private static void download(Workbook workbook, String fileName) throws  IOException {
        Context ctx = Context.current();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        workbook.write(os);
        ctx.headerSet("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"utf-8"));
        ctx.contentType("application/octet-stream;charset=UTF-8");
        ctx.headerSet("Pargam", "no-cache");
        ctx.headerSet("Cache-Control", "no-cache");
        ctx.contentLength(os.size());
        ctx.headerSet("Content-Length", "" + os.size());
        OutputStream outputStream = ctx.outputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }
    private static void write(Workbook workbook, String fileName,String path) throws  IOException {
        //声明输出流
        OutputStream os = null;
        //设置响应头
        setResponseHeader(fileName);
        //
        File file = new File(path+"/"+fileName);
        os = new FileOutputStream(file);
        workbook.write(os);
        // 关闭输出流
        if (os != null) {
            os.close();
        }
    }


    /**
     * 设置浏览器下载响应头
     */
    private static void setResponseHeader( String fileName) {
        try {
            Context ctx = Context.current();
            fileName = new String(fileName.getBytes(),"UTF-8");
            ctx.contentType("application/octet-stream;charset=UTF-8");
            ctx.headerSet("Pragma", "no-cache");
            ctx.headerSet("Cache-Control", "no-cache");
            ctx.headerSet("Content-Disposition", "attachment;filename="+ fileName);
        } catch (UnsupportedEncodingException e) {
            throw new MyRuntimeException(e.getMessage());
        }

    }
}
