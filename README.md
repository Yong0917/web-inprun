import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileParsing2 {

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        ResultSet result2 = null;

        // 현재 날짜에서 -1

        try {
            String jdbcDriver = "jdbc:mariadb://localhost:13306/ERD";
            String dbUser = "igloosec";
            String dbPass = "sp!dertm404040";
            conn = DriverManager.getConnection(jdbcDriver,dbUser,dbPass);
            stmt = conn.createStatement();

            String query1 = "select * from costfilelog where STATUS = 'S';";    //STATUS = 'S' 인 쿼리
            result = stmt.executeQuery(query1);

            //result = select * from costfilelog where STATUS = 'S'
            while(result.next()) {

                //LC_PATH 안에 csv 경로 추출
                String rs = result.getString("LC_PATH");
                System.out.println("CSV File  : " + rs);



                SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
                Date date = null;

                String rsd = result.getString("LC_PATH");       // 수정 : rsd를 빼고 rs로 수정
                String time1 = rsd.substring(33,41);                // csv파일명 날짜 분리 ex) 20210405
                date = format1.parse(time1);

                SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd");

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, -1);                 // 날짜에서 -1 ex) 20210404
                String today1 = format2.format(cal.getTime());



                File csvfile = new File(rs);
                String line = null;
                String[] coordi;
                String coord3;

                // rs에 LC_PATH에 경로가 담겨져있음.(한줄 씩 파일 읽음)
                BufferedReader bufReader = new BufferedReader(new FileReader(csvfile));
                for (int i = 1; (line = bufReader.readLine()) != null; i++) {           // path csv 하나씩 읽어서 한줄씩 parse
                    if (i == 1) {             //header 출력 x
                        continue;
                    }
                    else {
                        coord3 = line.replace("\", " , "\"# ");
                        coordi = coord3.split(",");

                        // insert csvfile 테이블
                        //coordi[4] = tenantId  , coordi[29] = rscListId , date = today, amount = coordi[38]
                        //rscListId 중복되면 업데이트
                        String qry4 = "insert into USED_AMOUNT(rscListId,date,amount,partnerName,resellerName,resellerMpnId,customerTenantId" +
                                ",customerName,servicePeriodEndDate,servicePeriodStartDate,date2,serviceFamily,productOrderId," +
                                "productOrderName,consumedService,meterId,meterName,meterCategory,meterSubCategory,meterRegion,ProductId," +
                                "ProductName,SubscriptionId,subscriptionName,publisherType,publisherName,resourceGroupName,ResourceId," +
                                "resourceLocation,location,effectivePrice,quantity,unitOfMeasure,chargeType,billingCurrency,pricingCurrency" +
                                ",costInBillingCurrency,costInUsd,exchangeRatePricingToBilling,exchangeRateDate,serviceInfo2,additionalInfo," +
                                "tags,PayGPrice,frequency) " +
                                "values ('" + coordi[29] + "' ,'" + today1 + "' ,'" + coordi[38] + "','" + coordi[1] + "','" + coordi[2] + "'" +
                                ",'" + coordi[3] + "','" + coordi[4] + "','" + coordi[5] + "','" + coordi[9] + "','" + coordi[10] + "','" + coordi[11] + "'" +
                                ",'" + coordi[12] + "','" + coordi[13] + "','" + coordi[14] + "','" + coordi[15] + "','" + coordi[16] + "','" + coordi[17] + "'" +
                                ",'" + coordi[18] + "','" + coordi[19] + "','" + coordi[20] + "','" + coordi[21] + "','" + coordi[22] + "','" + coordi[23] + "'" +
                                ",'" + coordi[24] + "','" + coordi[25] + "','" + coordi[27] + "','" + coordi[28] + "','" + coordi[29] + "','" + coordi[30] + "'" +
                                ",'" + coordi[31] + "','" + coordi[32] + "','" + coordi[33] + "','" + coordi[34] + "','" + coordi[35] + "','" + coordi[36] + "'" +
                                ",'" + coordi[37] + "','" + coordi[38] + "','" + coordi[39] + "','" + coordi[40] + "','" + coordi[41] + "','" + coordi[43] + "'" +
                                ",'" + coordi[44] + "','" + coordi[45] + "','" + coordi[46] + "','" + coordi[47] + "')" +
                                "ON DUPLICATE KEY UPDATE amount='"+ coordi[38] +"',partnerName='"+coordi[1]+"',resellerName='"+coordi[2]+"',resellerMpnId='"+coordi[3]+"'" +
                                ",customerTenantId='"+coordi[4]+"',customerName='"+coordi[5]+"',servicePeriodEndDate='"+coordi[9]+"',servicePeriodStartDate='"+coordi[10]+"',date2='"+coordi[11]+"'" +
                                ",serviceFamily='"+coordi[12]+"',productOrderId='"+coordi[13]+"',productOrderName='"+coordi[14]+"',consumedService='"+coordi[15]+"',meterId='"+coordi[16]+"'" +
                                ",meterName='"+coordi[17]+"',meterCategory='"+coordi[18]+"',meterSubCategory='"+coordi[19]+"',meterRegion='"+coordi[20]+"',ProductId='"+coordi[21]+"'" +
                                ",ProductName='"+coordi[22]+"',SubscriptionId='"+coordi[23]+"',subscriptionName='"+coordi[24]+"',publisherType='"+coordi[25]+"',publisherName='"+coordi[27]+"'" +
                                ",resourceGroupName='"+coordi[28]+"',ResourceId='"+coordi[29]+"',resourceLocation='"+coordi[30]+"',location='"+coordi[31]+"',effectivePrice='"+coordi[32]+"'" +
                                ",quantity='"+coordi[33]+"',unitOfMeasure='"+coordi[34]+"',chargeType='"+coordi[35]+"',billingCurrency='"+coordi[36]+"',pricingCurrency='"+coordi[37]+"',costInBillingCurrency='"+coordi[38]+"'" +
                                ",costInUsd='"+coordi[39]+"',exchangeRatePricingToBilling='"+coordi[40]+"',exchangeRateDate='"+coordi[41]+"',serviceInfo2='"+coordi[43]+"',additionalInfo='"+coordi[44]+"',tags='"+coordi[45]+"'" +
                                ",PayGPrice='"+coordi[46]+"',frequency='"+coordi[47]+"';";
                        stmt.execute(qry4);
                    }
                }

                //STATUS 'S' ----> 'C'
                String qry2 = "update costfilelog set STATUS = 'C' where LC_PATH = '" + rs + "' ;";
                stmt.executeUpdate(qry2);


                //LC_PATH 'received' ----> 'complete'
                String LC_PATH_After = rs.replace("/usr/azureCostFile/received/", "/usr/azureCostFile/complete/");
                String qry3 = "update costfilelog set LC_PATH = '" + LC_PATH_After + "' where LC_PATH = '" + rs + "';";
                stmt.executeUpdate(qry3);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}


