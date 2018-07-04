function maskTelefone(name) {
	var inputMask = PF(name);
	var numero = inputMask.getValue().replace(/[^0-9]/g, '');
	var change = false;
	if(numero.length >= 11){
		if(inputMask.cfg.mask != '(99) 99999-9999'){
			change = true;
		}
		inputMask.cfg.mask = '(99) 99999-9999';
	}else{           
		if(inputMask.cfg.mask != '(99) 9999-9999?9'){
			change = true;
		}
		inputMask.cfg.mask = '(99) 9999-9999?9';
	}
	if(change){
		inputMask.setValue(inputMask.getValue());
	}
}
/**
 * preenche valor com o caractere informado com o tamanho informado
 */
function leftPad(caractere, tamanho, valor){
	var str = "" + valor;
	var pad = "";
	for(i = 0;i < tamanho; i++){
		pad += caractere;
	}
	return pad.substring(0, pad.length - str.length) + str;
}