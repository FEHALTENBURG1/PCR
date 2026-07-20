package br.pcr;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servidor HTTP leve (somente JDK, sem dependências) para o painel
 * de assistência à PCR. Serve o frontend em /static e expõe uma
 * API simples de registro de eventos em /api/eventos.
 *
 * Uso:
 *   javac -d out src/main/java/br/pcr/Main.java
 *   java -cp out:src/main/resources br.pcr.Main [porta]
 */
public class Main {

    private static final List<String> eventos = new ArrayList<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        server.createContext("/", Main::servirEstatico);
        server.createContext("/api/eventos", Main::apiEventos);

        server.setExecutor(null);
        server.start();
        System.out.println("Assistente de PCR rodando em http://localhost:" + porta);
        System.out.println("Pressione Ctrl+C para encerrar.");
    }

    /** Serve arquivos de src/main/resources/static (via classpath). */
    private static void servirEstatico(HttpExchange ex) throws IOException {
        String caminho = ex.getRequestURI().getPath();
        if (caminho.equals("/")) caminho = "/index.html";

        // Evita path traversal
        if (caminho.contains("..")) {
            responder(ex, 400, "text/plain", "Requisição inválida".getBytes(StandardCharsets.UTF_8));
            return;
        }

        try (InputStream in = Main.class.getResourceAsStream("/static" + caminho)) {
            if (in == null) {
                responder(ex, 404, "text/plain; charset=utf-8",
                        "Não encontrado".getBytes(StandardCharsets.UTF_8));
                return;
            }
            responder(ex, 200, tipoMime(caminho), in.readAllBytes());
        }
    }

    /** GET lista eventos; POST registra um evento (corpo = descrição). */
    private static void apiEventos(HttpExchange ex) throws IOException {
        String metodo = ex.getRequestMethod();
        if (metodo.equals("POST")) {
            String corpo = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8).trim();
            if (!corpo.isEmpty()) {
                synchronized (eventos) {
                    eventos.add(LocalDateTime.now().format(FMT) + " — " + corpo);
                }
            }
            responder(ex, 201, "application/json", "{\"ok\":true}".getBytes(StandardCharsets.UTF_8));
        } else if (metodo.equals("GET")) {
            StringBuilder json = new StringBuilder("[");
            synchronized (eventos) {
                for (int i = 0; i < eventos.size(); i++) {
                    if (i > 0) json.append(',');
                    json.append('"').append(eventos.get(i).replace("\"", "\\\"")).append('"');
                }
            }
            json.append(']');
            responder(ex, 200, "application/json; charset=utf-8",
                    json.toString().getBytes(StandardCharsets.UTF_8));
        } else if (metodo.equals("DELETE")) {
            synchronized (eventos) { eventos.clear(); }
            responder(ex, 200, "application/json", "{\"ok\":true}".getBytes(StandardCharsets.UTF_8));
        } else {
            responder(ex, 405, "text/plain", "Método não permitido".getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void responder(HttpExchange ex, int status, String tipo, byte[] corpo) throws IOException {
        ex.getResponseHeaders().set("Content-Type", tipo);
        ex.sendResponseHeaders(status, corpo.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(corpo);
        }
    }

    private static String tipoMime(String caminho) {
        if (caminho.endsWith(".html")) return "text/html; charset=utf-8";
        if (caminho.endsWith(".css"))  return "text/css; charset=utf-8";
        if (caminho.endsWith(".js"))   return "application/javascript; charset=utf-8";
        if (caminho.endsWith(".svg"))  return "image/svg+xml";
        if (caminho.endsWith(".png"))  return "image/png";
        return "application/octet-stream";
    }
}
