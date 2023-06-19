package org.example;
//web scraping
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//conexão bd
import java.sql.*;
//data
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
//ping
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//data
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
//previsão temporal
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;


public class Respostas {
    //    https://auth-db814.hstgr.io/index.php?route=/&route=%2F&db=u265170636_bdandre
    String jdbcUrl = "URL_DO_BANCO";
    String username = "USUARIO_DO_BANCO";
    String password = "SENHA_DO_BANCO";

    
    //Weather API
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=CIDADEa&appid=CHAVE_API&units=metric";

    public void autoDeleteCompromisso(){
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM sentinela WHERE datad LIKE '__/__/____' ORDER BY SUBSTRING(datad, 4, 2), SUBSTRING(datad, 1, 2), SUBSTRING(datad, 7, 4);";
            ResultSet resultSet = statement.executeQuery(sql);

            LocalDate dataAtual = LocalDate.now();

            while (resultSet.next()) {

                String id = resultSet.getString("id_compr");
                String datad = resultSet.getString("datad");



                LocalDate dataCompromisso = LocalDate.parse(datad, DateTimeFormatter.ofPattern("dd/MM/yyyy"));


                long diferencaDias = ChronoUnit.DAYS.between(dataAtual, dataCompromisso);



                if (diferencaDias < 0) {
                    String sql2 = "DELETE FROM sentinela WHERE id_compr = ?";

                    PreparedStatement deleteStatement = connection.prepareStatement(sql2);
                    deleteStatement.setString(1, id);

                    deleteStatement.executeUpdate();

                    deleteStatement.close();
                }
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("\u001B[31mErro ao conectar ao MySQL: \u001B[0m" + e.getMessage());
        }
    }

    void autoSelectCompromisso(){
        try {

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            Statement statement = connection.createStatement();
            //vai ordenar por mês dia e ano
            String sql = "SELECT * FROM sentinela WHERE datad LIKE '__/__/____' ORDER BY SUBSTRING(datad, 4, 2), SUBSTRING(datad, 1, 2), SUBSTRING(datad, 7, 4);";
            ResultSet resultSet = statement.executeQuery(sql);

            LocalDate dataAtual = LocalDate.now();

            while (resultSet.next()) {
                String assunto = resultSet.getString("assunto");
                String datad = resultSet.getString("datad");

                LocalDate dataCompromisso = LocalDate.parse(datad, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                long diferencaDias = ChronoUnit.DAYS.between(dataAtual, dataCompromisso);

                if (diferencaDias <= 1) {
                    System.out.println("\u001B[31m\nCOMPROMISSO: " + assunto.strip() + " VENCE HOJE\u001B[0m");
                }

            }

            resultSet.close();
            statement.close();

            connection.close();
        } catch (SQLException e) {
            System.out.println("\u001B[31mErro ao conectar ao MySQL: \u001B[0m" + e.getMessage());
        }

    }

    void contagemCasamento(){
        try {

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            Statement statement = connection.createStatement();
            //vai ordenar por mês dia e ano
            String sql = "SELECT datad FROM sentinela WHERE id_compr = 6 AND datad LIKE '__/__/____' ORDER BY SUBSTRING(datad, 4, 2), SUBSTRING(datad, 1, 2), SUBSTRING(datad, 7, 4);";
            ResultSet resultSet = statement.executeQuery(sql);

            LocalDate dataAtual = LocalDate.now();

            while (resultSet.next()) {
                String datad = resultSet.getString("datad");

                LocalDate dataCompromisso = LocalDate.parse(datad, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                long diferencaDias = ChronoUnit.DAYS.between(dataAtual, dataCompromisso);

                System.out.println("\u001B[33m\nFaltam: " + diferencaDias + " PARA O CASAMENTO\u001B[0m");


            }

            resultSet.close();
            statement.close();

            connection.close();
        } catch (SQLException e) {
            System.out.println("\u001B[31mErro ao conectar ao MySQL: \u001B[0m" + e.getMessage());
        }
    }

    public void saudacoes(){

        Date dataHoraAtual = new Date();
        SimpleDateFormat formatador = new SimpleDateFormat("HH");
        String horaFormatada = formatador.format(dataHoraAtual);
        int horaFormatadaInt = Integer.parseInt(horaFormatada);

        if (horaFormatadaInt > 12 && horaFormatadaInt < 18) {
            System.out.println("\u001B[34;1mBOA TARDE ADMINISTRADOR\u001B[0m");
        }else if (horaFormatadaInt >= 18 && horaFormatadaInt <= 23) {
            System.out.println("\u001B[34;1mBOA NOITE ADMINISTRADOR\u001B[0m");
        }else{
            System.out.println("\u001B[34;1mBOM DIA ADMINISTRADOR\u001B[0m");
        }
    }

    public void retornaCompromisso() {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            Statement statement = connection.createStatement();
            //vai ordenar por mês dia e ano
            String sql = "SELECT * FROM sentinela WHERE datad LIKE '__/__/____' ORDER BY SUBSTRING(datad, 4, 2), SUBSTRING(datad, 1, 2), SUBSTRING(datad, 7, 4);";
            ResultSet resultSet = statement.executeQuery(sql);

            LocalDate dataAtual = LocalDate.now();

            while (resultSet.next()) {
                String id = resultSet.getString("id_compr");
                String assunto = resultSet.getString("assunto");
                String lugar = resultSet.getString("lugar");
                String datad = resultSet.getString("datad");
                String hora = resultSet.getString("hora");
                String observacoes = resultSet.getString("observacoes");
                String observacoes2 = resultSet.getString("observacoes2");
                String avisos = resultSet.getString("avisos");

                LocalDate dataCompromisso = LocalDate.parse(datad, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                long diferencaDias = ChronoUnit.DAYS.between(dataAtual, dataCompromisso);

                if (diferencaDias <= 3) {
                    System.out.println("\u001B[31m\nID: \u001B[0m" + id.strip() + "\u001B[31m\nCOMPROMISSO: \u001B[0m" + assunto.strip() + " | \u001B[31mLUGAR: \u001B[0m" + lugar.strip() + " | \u001B[31mDATA: \u001B[0m" + datad.strip() + " | \u001B[31mHORÁRIO: \u001B[0m" + hora.strip() + " | \u001B[31mOBS: \u001B[0m" + observacoes.strip() + " | \u001B[31mOBS2: \u001B[0m" + observacoes2.strip() + " | \u001B[31mAVISOS: \u001B[0m" + avisos.strip());
                } else if (diferencaDias <= 7) {
                    System.out.println("\u001B[33m\nID: \u001B[0m" + id.strip() + "\u001B[33m\nCOMPROMISSO: \u001B[0m" + assunto.strip() + " | \u001B[33mLUGAR: \u001B[0m" + lugar.strip() + " | \u001B[33mDATA: \u001B[0m" + datad.strip() + " | \u001B[33mHORÁRIO: \u001B[0m" + hora.strip() + " | \u001B[33mOBS: \u001B[0m" + observacoes.strip() + " | \u001B[33mOBS2: \u001B[0m" + observacoes2.strip() + " | \u001B[33mAVISOS: \u001B[0m" + avisos.strip());
                } else {
                    System.out.println("\u001B[32m\nID: \u001B[0m" + id.strip() + "\u001B[32m\nCOMPROMISSO: \u001B[0m" + assunto.strip() + " | \u001B[32mLUGAR: \u001B[0m" + lugar.strip() + " | \u001B[32mDATA: \u001B[0m" + datad.strip() + " | \u001B[32mHORÁRIO: \u001B[0m" + hora.strip() + " | \u001B[32mOBS: \u001B[0m" + observacoes.strip() + " | \u001B[32mOBS2: \u001B[0m" + observacoes2.strip() + " | \u001B[32mAVISOS: \u001B[0m" + avisos.strip());
                }

            }

            resultSet.close();
            statement.close();

            connection.close();
        } catch (SQLException e) {
            System.out.println("\u001B[31mErro ao conectar ao MySQL: \u001B[0m" + e.getMessage());
        }
    }

    public void cadastraComprimisso(Scanner entrada){

        try {

            System.out.print("DIGITE UM NOVO COMPROMISSO OU 'SAIR' PARA CANCELAR: ");
            String comprom = entrada.nextLine().toUpperCase();
            if (comprom.equalsIgnoreCase("sair")) {
                return;
            } else {
                System.out.print("\nDIGITE UM LUGAR OU 'SAIR' PARA CANCELAR: ");
                String lug = entrada.nextLine().toUpperCase();
                if (lug.equalsIgnoreCase("sair")) {
                    return;

                }
                else {
                    System.out.print("\nDIGITE UMA DATA OU 'SAIR' PARA CANCELAR: ");
                    String rdata = entrada.nextLine().toUpperCase();
                    if (rdata.equalsIgnoreCase("sair")) {
                        return;
                    } else {
                        System.out.print("\nDIGITE UMA HORA OU 'SAIR' PARA CANCELAR: ");
                        String rhora = entrada.nextLine().toUpperCase();
                        if (rhora.equalsIgnoreCase("sair")) {
                            return;
                        } else {
                            System.out.print("\nDIGITE UMA OBSERVAÇÃO OU 'SAIR' PARA CANCELAR: ");
                            String observ = entrada.nextLine().toUpperCase();
                            if (observ.equalsIgnoreCase("sair")) {
                                return;
                            } else {
                                System.out.print("\nDIGITE OUTRA OBSERVAÇÃO OU 'SAIR' PARA CANCELAR: ");
                                String observ2 = entrada.nextLine().toUpperCase();
                                if (observ2.equalsIgnoreCase("sair")) {
                                    return;
                                } else {
                                    System.out.print("\nDIGITE UM AVISO OU 'SAIR' PARA CANCELAR: ");
                                    String avisos = entrada.nextLine().toUpperCase();
                                    if (avisos.equalsIgnoreCase("sair")) {
                                        return;
                                    } else {
                                        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                                        String sql = "INSERT INTO sentinela (assunto, lugar, datad, hora, observacoes, observacoes2, avisos) VALUES (?, ?, ?, ?, ?, ?, ?);";

                                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                            statement.setString(1, comprom);
                                            statement.setString(2, lug);
                                            statement.setString(3, rdata);
                                            statement.setString(4, rhora);
                                            statement.setString(5, observ);
                                            statement.setString(6, observ2);
                                            statement.setString(7, avisos);

                                            int rowsAffected = statement.executeUpdate();
                                            connection.close();

                                            System.out.println("Inserção realizada com sucesso. Linhas afetadas: " + rowsAffected);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mErro ao conectar ao MySQL: \u001B[0m" + e.getMessage());
        }
    }

    public void editaCompromisso(Scanner entrada){

        try{
            retornaCompromisso();
            System.out.print("\nQUAL COMPROMISSO (PEGUE A ID) VOCÊ DESEJA EDITAR/APAGAR ('sair' para sair) \n>>");
            String recebeId = entrada.nextLine();
            if (recebeId.equalsIgnoreCase("sair")){
                return;
            }
            int pegaID = Integer.parseInt(recebeId);
            System.out.println(pegaID);

            System.out.print("\nVOCÊ DESEJA EXCLUIR OU EDITAR? ('sair' para sair) \n>>");
            String recebeResposta = entrada.nextLine();

            if (recebeResposta.equalsIgnoreCase("excluir")){
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

                String sql2 = "DELETE FROM sentinela WHERE id_compr = ? ;";

                PreparedStatement deleteStatement = connection.prepareStatement(sql2);
                deleteStatement.setInt(1, pegaID);

                deleteStatement.close();
                connection.close();
            } else if (recebeResposta.equalsIgnoreCase("editar")) {
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

                String sql = "SELECT * FROM sentinela WHERE id_compr = ? ;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, pegaID);  // Define o valor do parâmetro "id_compr"

                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.print("\nQUAL CAMPO VOCÊ DESEJA EDITAR? ");
                String informaCampo = entrada.nextLine();

                resultSet.close();
                preparedStatement.close();
                connection.close();

                if (informaCampo.equalsIgnoreCase("compromisso")) {

                    System.out.print("\nDIGITE O NOVO COMPROMISSO \n>>");
                    String novoValorDoCampo = entrada.nextLine();

                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET assunto = ? WHERE id_compr =? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();

                }else if (informaCampo.equalsIgnoreCase("lugar")) {

                    System.out.print("\nDIGITE O NOVO LUGAR \n>>");
                    String novoValorDoCampo = entrada.nextLine();

                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET lugar = ? WHERE id_compr = ? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();

                }else if (informaCampo.equalsIgnoreCase("data")) {

                    System.out.print("\nDIGITE A NOVA DATA \n>>");
                    String novoValorDoCampo = entrada.nextLine();

                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET datad = ? WHERE id_compr = ? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();
                }else if (informaCampo.equalsIgnoreCase("horario")) {

                    System.out.print("\nDIGITE O NOVO HORÁRIO \n>>");
                    String novoValorDoCampo = entrada.nextLine();

                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET hora = ? WHERE id_compr = ? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();
                }else if (informaCampo.equalsIgnoreCase("obs")) {

                    System.out.print("\nDIGITE A NOVA OBSERVAÇÃO \n>>");
                    String novoValorDoCampo = entrada.nextLine();

                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET observacoes = ? WHERE id_compr = ? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();
                }else if (informaCampo.equalsIgnoreCase("obs2")) {

                    System.out.print("\nDIGITE A NOVA OBSERVAÇÃO 2 \n>>");
                    String novoValorDoCampo = entrada.nextLine();

                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET observacoes2 = ? WHERE id_compr = ? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();
                }else if (informaCampo.equalsIgnoreCase("avisos")) {

                    System.out.print("\nDIGITE O NOVO AVISO \n>>");
                    String novoValorDoCampo = entrada.nextLine();


                    Connection updateConnection = DriverManager.getConnection(jdbcUrl, username, password);
                    String updateSql = "UPDATE sentinela SET avisos = ? WHERE id_compr = ? ;";

                    PreparedStatement updatePreparedStatement = updateConnection.prepareStatement(updateSql);
                    updatePreparedStatement.setString(1, novoValorDoCampo);  // Define o novo valor do campo "assunto"
                    updatePreparedStatement.setInt(2, pegaID);  // Define o valor do parâmetro "id_compr"

                    updatePreparedStatement.execute();
                    updatePreparedStatement.close();
                    updateConnection.close();
                }
            }



        }catch (SQLException e){
            System.out.println("\u001B[31mErro ao conectar ao MySQL: \u001B[0m" + e.getMessage());
        }



    }

    void mostraNoticias(Scanner entrada){
        int i=0;
        try{
            String url = "https://www.cnnbrasil.com.br";

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("h3");

            System.out.print("\u001B[33m\nDESEJA TRAZER APENAS AS 30 PRIMEIRAS NOTICIAS?(s/n) \u001B[0m");
            String resp = entrada.nextLine();

            if (resp.equalsIgnoreCase("s")){
                for (Element elemento : links){
                    System.out.println("\u001B[32m\nTÓPICO "+i+" -> \u001B[0m" + elemento.text());
                    i++;
                    if (i == 30){
                        break;
                    }
                }
            }
            else{
                for (Element elemento : links){
                    System.out.println("\u001B[32m\nTÓPICO "+i+" -> \u001B[0m" + elemento.text());
                    i++;
                }
            }


            //LINKS ABAIXO
//            Elements links = doc.select("a[href]");
//            for (Element elemento : links){
//
//                String conteudo = elemento.attr("href");
//                if (conteudo.contains("")){
//                    System.out.println("LINK: " + conteudo);
//                }
//            }

            //ID ESPECÍFICA
//            import org.jsoup.Jsoup;
//            import org.jsoup.nodes.Document;
//            import org.jsoup.nodes.Element;
//
//            public class ExemploJsoup {
//                public static void main(String[] args) {
//                    String html = "<html><body><p id=\"meuParagrafo\">Texto do parágrafo</p></body></html>";
//
//                    Document doc = Jsoup.parse(html);
//                    Element paragrafo = doc.getElementById("meuParagrafo");
//
//                    if (paragrafo != null) {
//                        String textoParagrafo = paragrafo.text();
//                        System.out.println(textoParagrafo);
//                    }
//                }
//            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void pingSite(Scanner entrada){
        try {
            System.out.print("\u001B[33m\nQUAL SITE/IP VOCÊ DESEJA DAR O PING? \u001B[0m");
            String ping = entrada.nextLine();

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ping "+ping);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Aguarda até que o processo seja concluído
            int exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void filmesCartaz(){
        int cont = 0;
        try{
            String url = "https://www.centerplex.com.br/cinema/viasul.html";

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("h3");

            for (Element elemento : links){
                if (cont == 0){
                    System.out.println("\u001B[33m\nLOCAL -> \u001B[0m" + elemento.text());
                    cont++;
                }else{
                    System.out.println("\u001B[32m\nFILME -> \u001B[0m" + elemento.text());
                }


            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //previsão do tempo
    static void previsaoTemporal() throws IOException {
        String weather = getWeather();
        parseWeatherData(weather);
    }
    static String getWeather() throws IOException {
        OkHttpClient client = new OkHttpClient();

        String apiUrl = API_URL + "&lang=pt";

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    static void parseWeatherData(String weatherData) {
        JSONObject jsonObject = new JSONObject(weatherData);

        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        String description = weatherObject.getString("description");

        JSONObject mainObject = jsonObject.getJSONObject("main");
        double temp = mainObject.getDouble("temp");
        double tempMin = mainObject.getDouble("temp_min");
        double tempMax = mainObject.getDouble("temp_max");
        int humidity = mainObject.getInt("humidity");

        String cityName = jsonObject.getString("name");

        System.out.println("\u001B[35mDescrição: \u001B[0m" + description);
        System.out.println("\u001B[35mTemperatura: \u001B[0m" + temp + "°C");
        System.out.println("\u001B[35mTemperatura Mínima: \u001B[0m" + tempMin + "°C");
        System.out.println("\u001B[35mTemperatura Máxima: \u001B[0m" + tempMax + "°C");
        System.out.println("\u001B[35mUmidade: \u001B[0m" + humidity + "%");
        System.out.println("\u001B[35mCidade: \u001B[0m" + cityName);
    }



}
