(function () {
    function qs(sel, root) {
        return (root || document).querySelector(sel);
    }

    function qsa(sel, root) {
        return Array.prototype.slice.call((root || document).querySelectorAll(sel));
    }

    /* Abas do painel */
    var tabs = qsa('.aba-painel');
    var secoes = {
        dados: qs('#secaoDados'),
        assinaturas: qs('#secaoAssinaturas'),
        enderecos: qs('#secaoEnderecos')
    };

    function ativarSecao(nome) {
        Object.keys(secoes).forEach(function (key) {
            var el = secoes[key];
            if (!el) return;
            var ativa = key === nome;
            el.classList.toggle('is-active', ativa);
            el.setAttribute('aria-hidden', ativa ? 'false' : 'true');
        });
        tabs.forEach(function (tab) {
            var secao = tab.getAttribute('data-secao');
            var ativa = secao === nome;
            tab.classList.toggle('is-active', ativa);
            tab.setAttribute('aria-selected', ativa ? 'true' : 'false');
        });
    }

    tabs.forEach(function (tab) {
        tab.addEventListener('click', function () {
            var nome = tab.getAttribute('data-secao');
            if (nome) ativarSecao(nome);
        });
    });

    /* Popup de assinatura (usa classe .is-open — ver painel.css) */
    var backdrop = qs('#popupBackdrop');
    var btnFechar = qs('#popupClose');
    var btnFecharInf = qs('#popupFecharInferior');

    function fecharPopup() {
        if (!backdrop) return;
        backdrop.classList.remove('is-open');
        backdrop.setAttribute('aria-hidden', 'true');
        document.body.classList.remove('popup-open');
    }

    function abrirPopup() {
        if (!backdrop) return;
        backdrop.classList.add('is-open');
        backdrop.setAttribute('aria-hidden', 'false');
        document.body.classList.add('popup-open');
    }

    if (backdrop) {
        backdrop.addEventListener('click', function (e) {
            if (e.target === backdrop) fecharPopup();
        });
    }
    if (btnFechar) btnFechar.addEventListener('click', fecharPopup);
    if (btnFecharInf) btnFecharInf.addEventListener('click', fecharPopup);

    qsa('.assinatura').forEach(function (card) {
        card.addEventListener('click', function () {
            abrirPopup();
        });
        card.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                abrirPopup();
            }
        });
    });

    /* Aba via ?aba= na URL */
    var params = new URLSearchParams(window.location.search);
    var aba = params.get('aba');
    if (aba && secoes[aba]) {
        ativarSecao(aba);
    }

    /* Mensagens de sucesso: ~2s visível e some */
    qsa('.cliente-toast--timed').forEach(function (el) {
        window.setTimeout(function () {
            el.classList.add('cliente-toast--gone');
            window.setTimeout(function () {
                if (el.parentNode) el.parentNode.removeChild(el);
            }, 420);
        }, 2000);
    });

    document.addEventListener('keydown', function (e) {
        if (e.key !== 'Escape') return;
        if (backdrop && backdrop.classList.contains('is-open')) {
            fecharPopup();
        }
    });
})();
