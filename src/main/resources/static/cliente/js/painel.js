(function () {
    function qs(sel, root) {
        return (root || document).querySelector(sel);
    }

    function qsa(sel, root) {
        return Array.prototype.slice.call((root || document).querySelectorAll(sel));
    }

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

    var backdrop = qs('#popupBackdrop');
    var btnFechar = qs('#popupClose');
    var btnFecharInf = qs('#popupFecharInferior');
    var popupImg = qs('#popupImg');
    var popupTitle = qs('#popupTitle');
    var popupDesc = qs('#popupDesc');
    var popupPreco = qs('#popupPreco');
    var btnDesativar = qs('#btnDesativarAssinatura');
    var formDesativar = qs('#formDesativarAssinatura');
    var desativarUrlTemplate = document.body.getAttribute('data-desativar-url-template') || '';

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

    function preencherPopup(card) {
        var id = card.getAttribute('data-id');
        var cardImg = qs('.assinatura__media img', card);
        var titleEl = qs('.assinatura__title', card);
        var nome = titleEl ? titleEl.textContent.trim() : '';
        var descricao = card.getAttribute('data-descricao') || '';
        var preco = card.getAttribute('data-preco') || '0,00';

        if (popupTitle) popupTitle.textContent = nome;
        if (popupDesc) popupDesc.textContent = descricao;
        if (popupImg && cardImg) {
            popupImg.src = cardImg.src;
            popupImg.alt = cardImg.alt || nome;
        }
        if (popupPreco) popupPreco.textContent = 'R$\u00a0' + preco;

        if (btnDesativar && formDesativar) {
            if (id && Number(id) > 0 && desativarUrlTemplate) {
                formDesativar.action = desativarUrlTemplate.replace('/0/', '/' + id + '/');
                btnDesativar.hidden = false;
            } else {
                formDesativar.removeAttribute('action');
                btnDesativar.hidden = true;
            }
        }
    }

    function abrirPopupFromCard(card) {
        preencherPopup(card);
        abrirPopup();
    }

    if (backdrop) {
        backdrop.addEventListener('click', function (e) {
            if (e.target === backdrop) fecharPopup();
        });
    }
    if (btnFechar) btnFechar.addEventListener('click', fecharPopup);
    if (btnFecharInf) btnFecharInf.addEventListener('click', fecharPopup);

    if (btnDesativar) {
        btnDesativar.addEventListener('click', function (e) {
            if (!window.confirm('Deseja realmente desativar esta assinatura?')) {
                e.preventDefault();
            }
        });
    }

    qsa('.assinatura').forEach(function (card) {
        card.addEventListener('click', function () {
            abrirPopupFromCard(card);
        });
        card.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                abrirPopupFromCard(card);
            }
        });
    });

    var params = new URLSearchParams(window.location.search);
    var aba = params.get('aba');
    if (aba && secoes[aba]) {
        ativarSecao(aba);
    }

    var assinaturaId = params.get('assinatura');
    if (assinaturaId) {
        ativarSecao('assinaturas');
        var cardDestaque = qs('#assinaturaCard-' + assinaturaId);
        if (cardDestaque) {
            abrirPopupFromCard(cardDestaque);
            window.setTimeout(function () {
                cardDestaque.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }, 120);
        }
    }

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
