class Nodo:
    def __init__(self, valor):
        self.valor = valor
        self.izquierdo = None
        self.derecho = None

class ArbolBinario:
    def __init__(self):
        self.raiz = None
    
    def insertar_lado(self, padre, valor, direccion):
        nuevo_nodo = Nodo(valor)
        
        if self.raiz is None:
            self.raiz = nuevo_nodo
            return True
        
        nodo_padre = self._buscar_nodo(self.raiz, padre)
        
        if nodo_padre is None:
            print(f"Error: No se encontró el nodo padre con valor {padre}")
            return False
        
        if direccion == 'izquierdo':
            if nodo_padre.izquierdo is None:
                nodo_padre.izquierdo = nuevo_nodo
                return True
            else:
                print(f"Error: El nodo {padre} ya tiene un hijo izquierdo")
                return False
        elif direccion == 'derecho':
            if nodo_padre.derecho is None:
                nodo_padre.derecho = nuevo_nodo
                return True
            else:
                print(f"Error: El nodo {padre} ya tiene un hijo derecho")
                return False
        else:
            print("Error: Dirección debe ser 'izquierdo' o 'derecho'")
            return False
    
    def _buscar_nodo(self, nodo_actual, valor):
        if nodo_actual is None:
            return None
        
        if nodo_actual.valor == valor:
            return nodo_actual
        
        resultado_izq = self._buscar_nodo(nodo_actual.izquierdo, valor)
        if resultado_izq:
            return resultado_izq
        
        return self._buscar_nodo(nodo_actual.derecho, valor)
    
    def mostrar_arbol(self):
        if not self.raiz:
            print("Árbol vacío")
            return
        
        self._mostrar_recursivo(self.raiz, 0, "")
    
    def _mostrar_recursivo(self, nodo, nivel, prefijo):
        if nodo is None:
            return
        
        print("  " * nivel + prefijo + str(nodo.valor))
        
        if nodo.izquierdo or nodo.derecho:
            if nodo.izquierdo:
                self._mostrar_recursivo(nodo.izquierdo, nivel + 1, "├── ")
            if nodo.derecho:
                self._mostrar_recursivo(nodo.derecho, nivel + 1, "└── ")

def construir_arbol_ejercicio():
    arbol = ArbolBinario()
    
    arbol.insertar_lado(None, 'A', 'izquierdo')
    arbol.insertar_lado('A', 'B', 'izquierdo')
    arbol.insertar_lado('A', 'C', 'derecho')
    arbol.insertar_lado('B', 'D', 'izquierdo')
    arbol.insertar_lado('B', 'E', 'derecho')
    arbol.insertar_lado('C', 'F', 'izquierdo')
    
    return arbol

# Ejecutar ejercicio 1
arbol = construir_arbol_ejercicio()
print("Árbol construido:")
arbol.mostrar_arbol()