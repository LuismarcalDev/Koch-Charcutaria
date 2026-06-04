package br.com.koch.servico.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Service
public class ServicoArquivoImagem {

    private final Path diretorioUpload;

    public ServicoArquivoImagem(
            @Value("${koch.upload.dir:uploads/cestas}") String diretorioUpload
    ) {
        this.diretorioUpload = Paths.get(diretorioUpload).toAbsolutePath().normalize();
    }

    public String salvarImagemCesta(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione uma imagem para a cesta.");
        }

        String contentType = arquivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("O arquivo deve ser uma imagem.");
        }

        Files.createDirectories(diretorioUpload);

        String extensao = extrairExtensao(arquivo.getOriginalFilename(), contentType);
        String nomeArquivo = UUID.randomUUID() + extensao;
        Path destino = diretorioUpload.resolve(nomeArquivo);
        arquivo.transferTo(destino);

        return "/uploads/cestas/" + nomeArquivo;
    }

    private static String extrairExtensao(String nomeOriginal, String contentType) {
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            String ext = nomeOriginal.substring(nomeOriginal.lastIndexOf('.')).toLowerCase(Locale.ROOT);
            if (ext.matches("\\.(jpg|jpeg|png|gif|webp)")) {
                return ext;
            }
        }
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }
}
