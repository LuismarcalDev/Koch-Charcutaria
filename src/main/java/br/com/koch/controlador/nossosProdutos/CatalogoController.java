package br.com.koch.controlador.nossosProdutos;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Year;
import java.util.List;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    @GetMapping
    public String catalogo(Model model) {

        model.addAttribute("sobreTitulo", "Como Surgiu a Koch Charcutaria");
        model.addAttribute("sobreTextos", List.of(
            "Mesmo antes de pensarmos em abrir uma empresa, nosso fundador Fernando apreciava " +
            "a verdadeira gastronomia, por isso em sua casa, ele se superava na cozinha para " +
            "que sua família consumisse alimentos bons e saudáveis.",

            "Além disso, ele tinha uma paixão pelo melhor da culinária. Com essa ideia, surge " +
            "a possibilidade de criar uma linha de produtos 100% artesanais. Dessa forma, nossa " +
            "empresa foi criada em 2019...",

            "Trabalhamos de forma natural, sem adição de glutamato monossódico e de corantes. " +
            "O que nos motiva é a sua saúde e segurança alimentar, além de proporcionar " +
            "momentos inesquecíveis com nossos produtos."
        ));

        model.addAttribute("bannerTexto", "Conheça algumas de nossas delícias");

        model.addAttribute("produtos", List.of(
            new ProdutoDTO("Panceta Defumada de Rolo",      "img1catalogo.jpeg",
                "Feita com barriga suína, a panceta passa por limpeza e pesagem antes da aplicação " +
                "dos temperos. Após a cura de 7 a 12 dias, as peças são lavadas, secas e amarradas " +
                "manualmente. Em seguida, passam pela defumação com lenhas frutíferas por 6 a 10 horas, " +
                "buscando desidratar o couro. Depois são resfriadas, passam por choque térmico e, " +
                "por fim, cortadas e embaladas manualmente."),

            new ProdutoDTO("Pancetta Arrotolata",            "img2catalogo.jpeg",
                "Feita com panceta suína sem couro, a carne é limpa, pesada e temperada, passando por " +
                "cura de 7 a 12 dias. Depois é lavada, seca, recheada com alecrim, enrolada e amarrada " +
                "manualmente. Segue para a defumação com lenhas frutíferas por 6 a 10 horas, sendo então " +
                "resfriada, submetida a choque térmico e finalmente fatiada e embalada."),

            new ProdutoDTO("Copa Defumada",                  "img3catalogo.jpeg",
                "A sobrepaleta suína passa por um processo cuidadoso de preparo: primeiro é feita a " +
                "toilette (limpeza e retirada de excessos), seguida da aplicação de um mix de temperos e " +
                "aditivos. A carne então entra no processo de cura por 21 dias em ambiente refrigerado " +
                "controlado. Após a cura, é lavada, seca, colocada em rede elástica e deixada descansar " +
                "por 12 horas antes da defumação. Na defumação, feita de forma escalonada e controlada, " +
                "a carne atinge 72 °C internamente, processo que dura de 6 a 12 horas."),

            new ProdutoDTO("Toscana com queijo de coalho",   "img4catalogo.jpeg",
                "A linguiça é produzida com carnes nobres (sobrepaleta, lombo e pancetta), que passam por " +
                "limpeza e moagem. A carne recebe um mix de temperos e é misturada até ficar homogênea. " +
                "Passa por cura de 24 horas. Antes do ensaque, são adicionados água e queijo coalho (20%). " +
                "As linguiças são embutidas em tripa suína natural, pesadas individualmente (450 g), " +
                "embaladas a vácuo e enviadas aos pontos de revenda."),

            new ProdutoDTO("Lombo defumado",                 "img5catalogo.jpeg",
                "O lombo suíno é preparado com toilette e aplicação de um mix de temperos e aditivos, " +
                "seguido de cura de 21 dias em ambiente refrigerado. Após a cura, as peças são lavadas, " +
                "secas e descansam 12 horas antes da defumação. Antes de defumar, cada peça recebe uma " +
                "borrifada de whisky Jack Daniel's e crosta de açúcar mascavo. A defumação é escalonada " +
                "até 72 °C internamente (6 a 12 horas), seguida de resfriamento e embalagem a vácuo.")
        ));

        model.addAttribute("anoAtual", Year.now().getValue());

        return "nossosProdutos/catalogo";
    }

    public static class ProdutoDTO {
        private final String nome;
        private final String imagem;
        private final String descricao;

        public ProdutoDTO(String nome, String imagem, String descricao) {
            this.nome = nome;
            this.imagem = imagem;
            this.descricao = descricao;
        }

        public String getNome()      { return nome; }
        public String getImagem()    { return imagem; }
        public String getDescricao() { return descricao; }
    }
}
