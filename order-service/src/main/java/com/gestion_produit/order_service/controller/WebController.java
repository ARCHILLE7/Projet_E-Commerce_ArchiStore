package com.gestion_produit.order_service.controller;

import com.gestion_produit.order_service.dto.OrderDTO;
import com.gestion_produit.order_service.dto.ProductDTO;
import com.gestion_produit.order_service.service.OrderService;
import com.gestion_produit.order_service.service.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductServiceClient productServiceClient;
    private final OrderService orderService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/products")
    public String products(Model model) {
        try {
            List<ProductDTO> products = productServiceClient.getAllProducts();
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des produits");
        }
        return "products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            model.addAttribute("orders", orders);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des commandes");
        }
        return "orders";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

}