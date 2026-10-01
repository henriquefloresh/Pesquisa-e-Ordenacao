def esta_contido(valor_pesquisa, lista):
    for item in lista:
        if item == valor_pesquisa:
            return True

    return False


lista = [6,1,3,7,4,2,9,7]

numero_pesquisa = 7