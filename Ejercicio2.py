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
    
    # EJERCICIO 2 - Función para contar nodos sin hijos
    def contar_nodos_sin_hijos(self):
        """
        Cuenta cuántos nodos en el árbol no tienen hijos (nodos hoja)
        Retorna: Número entero representando la cantidad de nodos sin hijos
        """
        def _contar_recursivo(nodo_actual):
            if nodo_actual is None:           # 1. Si el nodo es None (caso base)
                return 0                      # 2. Retorna 0 (no cuenta)
            
            # 3. Si NO tiene hijo izquierdo Y NO tiene hijo derecho
            if nodo_actual.izquierdo is None and nodo_actual.derecho is None:
                return 1                      # 4. Es un nodo hoja, cuenta 1
            
            # 5. Si tiene al menos un hijo, cuenta recursivamente en ambos subárboles
            contar_izquierdo = _contar_recursivo(nodo_actual.izquierdo)  # 6. Cuenta en subárbol izquierdo
            contar_derecho = _contar_recursivo(nodo_actual.derecho)      # 7. Cuenta en subárbol derecho
            
            return contar_izquierdo + contar_derecho  # 8. Retorna la suma total
        
        return _contar_recursivo(self.raiz)   # 9. Inicia el conteo desde la raíz

def construir_arbol_ejercicio():
    arbol = ArbolBinario()
    
    arbol.insertar_lado(None, 'A', 'izquierdo')
    arbol.insertar_lado('A', 'B', 'izquierdo')
    arbol.insertar_lado('A', 'C', 'derecho')
    arbol.insertar_lado('B', 'D', 'izquierdo')
    arbol.insertar_lado('B', 'E', 'derecho')
    arbol.insertar_lado('C', 'F', 'izquierdo')
    
    return arbol

# PROGRAMA PRINCIPAL - EJERCICIO 2
if __name__ == "__main__":
    # Construir el árbol del ejercicio 1
    arbol = construir_arbol_ejercicio()
    
    print("EJERCICIO 1 - ÁRBOL CONSTRUIDO:")
    print("=" * 40)
    arbol.mostrar_arbol()
    
    print("\n" + "=" * 40)
    print("EJERCICIO 2 - CONTAR NODOS SIN HIJOS")
    print("=" * 40)
    
    # Llamar a la función del ejercicio 2
    cantidad_hojas = arbol.contar_nodos_sin_hijos()
    
    print(f"RESULTADO: El árbol tiene {cantidad_hojas} nodos sin hijos")
    
    # Mostrar qué nodos son hojas
    print("\nNodos hoja (sin hijos): D, E, F")
    print("Estos nodos no tienen hijos izquierdo ni derecho")
    
    # Explicación adicional
    print("\n" + "=" * 40)
    print("EXPLICACIÓN:")
    print("=" * 40)
    print("Los nodos sin hijos (hojas) son aquellos que:")
    print("- No tienen hijo izquierdo (izquierdo is None)")
    print("- No tienen hijo derecho (derecho is None)")
    print("- En este árbol: D, E y F cumplen esta condición")