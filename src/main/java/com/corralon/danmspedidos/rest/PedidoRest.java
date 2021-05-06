package com.corralon.danmspedidos.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corralon.danmspedidos.domain.DetallePedido;
import com.corralon.danmspedidos.domain.Pedido;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/pedido")
@Api(value = "PedidoRest", description = "Permite gestionar los pedidos de la empresa? o cliente?")
public class PedidoRest {

	private static final List<Pedido> listaPedidos = new ArrayList<>();
	private static final List<Pedido> listaDetalle = new ArrayList<>();
	private static Integer ID_PEDIDO = 1;
	private static Integer ID_DETALLE = 1;
	
	// a) --- POST ---
	//i. Para crear un pedido usar el patrón de URL básico “/api/pedido”
	 @PostMapping
	    public ResponseEntity<Pedido> crear(@RequestBody Pedido nuevo){
	    	System.out.println(" pedido "+nuevo);
	        nuevo.setId(ID_PEDIDO++);
	        listaPedidos.add(nuevo);
	        return ResponseEntity.ok(nuevo);
	    }
	 
	 
	 
	 
	 // ii. Para agregar un item a un pedido usar “/api/pedido/{idPedido}/detalle”. Esto
	 // permite agregar un nuevo item a un pedido ya creado
	 @PostMapping(path = "/api/pedido/{idPedido}/detalle")
	    public ResponseEntity<DetallePedido> crearDetalle(@RequestBody DetallePedido nuevo, @PathVariable Integer idPedido){

	        Optional <Pedido> p =  listaPedidos
	                .stream()
	                .filter(unPed -> unPed.getId().equals(idPedido))
	                .findFirst();
	        System.out.println(" detallePedido "+nuevo);
	        nuevo.setId(ID_DETALLE++);
	        p.get().agregarItem(nuevo);//Segun la documentacion de Optional se usa asi , creeeeo
	        return ResponseEntity.ok(nuevo);
	        
	    }
	  
	 // b) --  PUT ---
	 
	 @PutMapping(path = "/{id}")
	    @ApiOperation(value = "Actualiza un pedido")
	    @ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Actualizado correctamente"),
	        @ApiResponse(code = 401, message = "No autorizado"),
	        @ApiResponse(code = 403, message = "Prohibido"),
	        @ApiResponse(code = 404, message = "El ID no existe")
	    })
	    public ResponseEntity<Pedido> actualizar(@RequestBody Pedido nuevo,  @PathVariable Integer id){
	        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
	        .filter(i -> listaPedidos.get(i).getId().equals(id))
	        .findFirst();

	        if(indexOpt.isPresent()){
	        	listaPedidos.set(indexOpt.getAsInt(), nuevo);
	            return ResponseEntity.ok(nuevo);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
	 // c) --DELETE -- 
	 
	 @DeleteMapping(path = "/{idPedido}")
	    public ResponseEntity<Pedido> borrar(@PathVariable Integer idPedido){
	        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
	        .filter(i -> listaPedidos.get(i).getId().equals(idPedido))
	        .findFirst();

	        if(indexOpt.isPresent()){
	            listaPedidos.remove(indexOpt.getAsInt());
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
	 
	 //falta para borrar detalle!
	 
	 
	 // d) -- GET --- 
	 
	 // i) POR ID
	 
	 @GetMapping(path = "/{idPedido}")
	    @ApiOperation(value = "Busca un cliente por id")
	    public ResponseEntity<Pedido> clientePorId(@PathVariable Integer idPedido){

	        Optional<Pedido> c =  listaPedidos
	                .stream()
	                .filter(unCli -> unCli.getId().equals(idPedido))
	                .findFirst();
	        return ResponseEntity.of(c);
	    }
	 
	 // ii) POR ID DE OBRA
	 
	 // iii) Por Cuit y/o ID de Cliente
	 
	 @GetMapping(path = "/cliente/{cliente}")
	    @ApiOperation(value = "Busca un cliente por ID o CUIT")
	    public ResponseEntity<Pedido> clientePorCuit(@PathVariable String cuit){

	        Optional<Pedido> c =  listaPedidos
	                .stream()
	                .filter(unPed -> unPed.get().equals(cuit))
	                .findFirst();
	        return ResponseEntity.of(c);
	    }
	 
	 // iv) Buscar detalle por ID: “/api/pedido/{idPedido}/detalle/{id}”
	 
	 
	 
	 
	
}
