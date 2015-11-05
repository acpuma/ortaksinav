package net.yazsoft.ors.distribute;


import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Named
@ViewScoped
public class Distribute {

    private static final Logger logger = Logger.getLogger(Distribute.class);
    private static final int BUFFER_SIZE = 6124;
    Map<Integer,List<Person>> sheetPersons=new LinkedHashMap<>();
    List<Person> persons;
    List<Room> rooms;
    Workbook workbook = null;
    Integer personCount;
    Boolean randomize=false;


    @Transactional
    public void handleFileUpload(FileUploadEvent event) {
        logger.info("UPLOADING FILE.......");

        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(Util.getUploadsFolder(),"/liste.xls"));
            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();

            Util.setFacesMessage("File name: "
                    + event.getFile().getFileName() + " yuklendi ");
                    //+ event.getFile().getSize() / 1024 + " Kb Content type: "
                    //+ event.getFile().getContentType() + " the file was uploaded.");
        } catch(Exception e){
            Util.setFacesMessageError(" the files were not uploaded : " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Person getNextPerson(Integer row) {
        try {
            //for (int i=1; i<sheetPersons.size()+1; i++) {
            Integer sheetNumber = row % (workbook.getNumberOfSheets() - 1);
            logger.info("LOG01310: SHEETNUMBER : " + sheetNumber + " , row : " + row);
            List<Person> pers = sheetPersons.get(sheetNumber + 1);
            Integer sheetSize=pers.size();
            if (sheetSize>0) {
                int value = 0;
                if (randomize) { //get random person
                    Random rand = new Random();
                    value = rand.nextInt(sheetSize);
                }
                if (pers.isEmpty()) return null;
                Person p = pers.get(value);
                if (p != null) {
                    sheetPersons.get(sheetNumber + 1).remove(value);
                    return p;
                }
            }
            //logger.info("LOG01290:person count: " + pers.size());
            //}
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadExcelDownload(){
        downloadExcel("dagitim.xls");
    }

    public void downloadExcelUpload() {
        downloadExcel("liste.xls");
    }

    public void downloadExcel(String filestr) {
        String filename=Util.getUploadsFolder() + "/"+filestr;
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+filestr);

        try {
            File file = new File(filename);
            FileInputStream fileIn = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();

            byte[] outputByte = new byte[4096];
            //copy binary contect to output stream
            while(fileIn.read(outputByte, 0, 4096) != -1)
            {
                out.write(outputByte, 0, 4096);
            }
            fileIn.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeExcel(String fileName){
        try {
            Workbook workbook = null;

            if (fileName.endsWith("xlsx")) {
                workbook = new XSSFWorkbook();
            } else if (fileName.endsWith("xls")) {
                workbook = new HSSFWorkbook();
            } else {
                throw new Exception("invalid file name, should be xls or xlsx");
            }

            Sheet sheet = workbook.createSheet("Dagitim");
            int rowIndex = 0;
            logger.info("LOG01330: PERSON COUNT:" + personCount);
            int index=0;
            for (Room room : rooms) {
                //if (room.getInclude().equals(1)) {
                    for (int i = 0; i < room.getCapacity(); i++) {
                        Person p = null;
                        while (p==null && rowIndex<personCount) {
                            p=getNextPerson(index);
                            if (index>723)  {
                                logger.info("LOG01340: PERSON : " + p + ", personcount: " + personCount);
                                logger.info("LOG01350:ROOM : " + room);
                            }
                            index++;
                        }
                        if (rowIndex>personCount) break;
                        logger.info("LOG01320: rowIndex: " + rowIndex + " , PERSON: " + p);
                        if (p!=null) {
                            rowIndex++;
                            Row row = sheet.createRow(rowIndex);
                            Cell cell0, cell1, cell2, cell3, cell4,cell5,cell6,cell7,cell8,cell9;
                            cell0 = row.createCell(0);
                            cell0.setCellValue(i + 1);
                            cell1 = row.createCell(1);
                            cell1.setCellValue(p.getSclass());
                            cell2 = row.createCell(2);
                            cell2.setCellValue(p.getNumber());
                            cell3 = row.createCell(3);
                            cell3.setCellValue(p.getName());
                            cell4 = row.createCell(4);
                            cell4.setCellValue(p.getSurname());
                            cell5 = row.createCell(5);
                            cell5.setCellValue(p.getBooklet());
                            cell6 = row.createCell(6);
                            cell6.setCellValue(room.getName());
                            cell7 = row.createCell(7);
                            cell7.setCellValue(p.getLesson1());
                            cell8 = row.createCell(8);
                            cell8.setCellValue(p.getLesson2());
                            cell9 = row.createCell(9);
                            cell9.setCellValue(p.getLesson3());
                        }
                    }
                //}
            }

            //lets write the excel data to file now
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.close();
            System.out.println(fileName + " written successfully");

            HttpServletResponse response=(HttpServletResponse)
                    FacesContext.getCurrentInstance().getExternalContext().getResponse();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition","attachment;filename=dagitim.xls");

            ServletOutputStream out=response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
            System.out.println("All done the report is done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readClasses() {
        Room room = null;
        try {
            //Get the FIRST sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);

            //every sheet has rows, iterate over them
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next(); //skip first row
            while (rowIterator.hasNext()) {
                //Get the row object
                Row row = rowIterator.next();
                room = new Room();
                //Every row has columns, get the column iterator and iterate over them
                Iterator<Cell> cellIterator = row.cellIterator();
                int index = 0;
                String value = null;
                while (cellIterator.hasNext() && index < 4) {
                    index++;
                    Cell cell = cellIterator.next();
                    try {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue().trim();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                        }
                        switch (index) {
                            case 1:room.setName(value);break;
                            case 2:room.setCapacity(Double.valueOf(value).intValue());break;
                            case 3:room.setInclude(Double.valueOf(value).intValue());break;
                            default:
                                logger.info("LOG01270: INDEX NONE : " + index);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //logger.info("LOG01240:VALUE : " + value);
                } //end of cell iterator
                rooms.add(room);
            } //end of rows iterator
            for (Room r:rooms) {
                logger.info("LOG01260:ROOM : " + r);
            }
            logger.info("LOG01290:room count: " + rooms.size());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void readExcel(String fileName) {
        try {
            //Create the input stream from the xlsx/xls file
            FileInputStream fis = new FileInputStream(fileName);

            //Create Workbook instance for xlsx/xls file input stream
            workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readStudents() {
        try {

            //Get the number of sheets in the xlsx file
            int numberOfSheets = workbook.getNumberOfSheets();
            personCount=0;

            Person person;
            //loop through each of the sheets
            for(int i=1; i < numberOfSheets; i++){

                //Get the nth sheet from the workbook
                Sheet sheet = workbook.getSheetAt(i);

                //every sheet has rows, iterate over them
                Iterator<Row> rowIterator = sheet.iterator();
                persons = new ArrayList<>();
                while (rowIterator.hasNext())
                {
                    //Get the row object
                    Row row = rowIterator.next();

                    person=new Person();
                    //Every row has columns, get the column iterator and iterate over them
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int index=0;
                    String value=null;
                    while (cellIterator.hasNext() && index<9) {
                        index++;
                        Cell cell = cellIterator.next();
                        try {
                            switch(cell.getCellType()){
                                case Cell.CELL_TYPE_STRING:
                                    value=cell.getStringCellValue().trim();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    value=String.valueOf(cell.getNumericCellValue());
                                    break;
                            }
                            if (value!=null) {
                                switch (index) {
                                    case 1:person.setId(Double.valueOf(value).intValue());break;
                                    case 2:person.setSclass(value);break;
                                    case 3:person.setNumber(Double.valueOf(value).intValue());break;
                                    case 4:person.setName(value);break;
                                    case 5:person.setSurname(value);break;
                                    case 6:person.setBooklet(value);break;
                                    case 7:person.setLesson1(value);break;
                                    case 8:person.setLesson2(value);break;
                                    case 9:person.setLesson3(value);break;
                                    default:
                                        logger.info("LOG01270: INDEX NONE : " + index);
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //logger.info("LOG01240:VALUE : " + value);
                    } //end of cell iterator

                    if ((findRoomByName(person.getSclass())!=null)
                            && (findRoomByName(person.getSclass()).getInclude()==1)) {
                        if (person.getNumber()!=null) {
                            persons.add(person);
                            personCount++;
                        }
                    }
                } //end of rows iterator
                sheetPersons.put(i,persons);

            } //end of sheets for loop

            for (int i=1; i<sheetPersons.size()+1; i++) {
                List<Person> pers=sheetPersons.get(i);
                for (Person p:pers){
                    //logger.info("SHEET :" + i + " Person : " + p);
                }
                logger.info("LOG01290:person count: " + pers.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putPersons() {
        List<Person> roomPersons;
        for (Room room:rooms) {
            roomPersons=findRoomPersons(room);
        }
    }

    private List<Person> findRoomPersons(Room room) {
        List list=new ArrayList<>();
        for (Person person:persons) {
            if (person.getSclass().equals(room.getName())) {
                list.add(person);
            }
        }
        return list;
    }
    private Room findRoomByName(String name) {
        for (Room room:rooms) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public void distribute() {
        rooms = new ArrayList<>();
        readExcel(Util.getUploadsFolder() + "/liste.xls");
        readClasses();
        readStudents();
        writeExcel(Util.getUploadsFolder() + "/dagitim.xls");
        //downloadExcel(Util.getUploadsFolder() + "/dagitim.xls");
    }


    /*    INNER CLASSES  *********/

    private class Room {
        String Name;
        Integer capacity;
        Integer include; //0 :no , 1 :yes

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public Integer getCapacity() {
            return capacity;
        }

        public void setCapacity(Integer capacity) {
            this.capacity = capacity;
        }

        public Integer getInclude() {
            return include;
        }

        public void setInclude(Integer include) {
            this.include = include;
        }

        @Override
        public String toString() {
            return "Room{" +
                    "Name='" + Name + '\'' +
                    ", capacity=" + capacity +
                    ", include=" + include +
                    '}';
        }
    }

    private class Person {
        Integer id;
        String Name;
        String Surname;
        String Sclass;
        Integer Number;
        Integer include;
        String booklet;
        String lesson1;
        String lesson2;
        String lesson3;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSurname() {
            return Surname;
        }

        public void setSurname(String surname) {
            Surname = surname;
        }

        public String getSclass() {
            return Sclass;
        }

        public void setSclass(String sclass) {
            Sclass = sclass;
        }

        public Integer getNumber() {
            return Number;
        }

        public void setNumber(Integer number) {
            Number = number;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getInclude() {
            return include;
        }

        public void setInclude(Integer include) {
            this.include = include;
        }

        public String getBooklet() {
            return booklet;
        }

        public void setBooklet(String booklet) {
            this.booklet = booklet;
        }

        public String getLesson1() {
            return lesson1;
        }

        public void setLesson1(String lesson1) {
            this.lesson1 = lesson1;
        }

        public String getLesson2() {
            return lesson2;
        }

        public void setLesson2(String lesson2) {
            this.lesson2 = lesson2;
        }

        public String getLesson3() {
            return lesson3;
        }

        public void setLesson3(String lesson3) {
            this.lesson3 = lesson3;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", Name='" + Name + '\'' +
                    ", Surname='" + Surname + '\'' +
                    ", Sclass='" + Sclass + '\'' +
                    ", Number=" + Number +
                    '}';
        }
    }

    public Boolean getRandomize() {
        return randomize;
    }

    public void setRandomize(Boolean randomize) {
        this.randomize = randomize;
    }
}
