package com.ibm.cicsdev.ejb.shop.web;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.ibm.cicsdev.ejb.CartBean;
import com.ibm.cicsdev.ejb.Item;

/**
 * JSF managed bean which proxies the relevant function to the JSF pages.
 * <p>
 * This ManagedBean is scoped to the session, so the injected bean is scoped to
 * the HTTP session.
 * 
 * @author Alexander Brown
 */
@ManagedBean
@SessionScoped
public class Cart
{
    /** The Cart EJB */
    @EJB
    private CartBean cart;
    
    /**
     * Proxy to {@link CartBean#getItems()}
     * 
     * @return All the items in the cart.
     */
    public Collection<Item> getItems()
    {
        return cart.getItems();
    }

    /**
     * Proxy to {@link CartBean#add(Item)}
     *
     * @param item The item to add.
     */
    public void add(Item item)
    {
        if (!cart.add(item))
        {
            FacesContext.getCurrentInstance().addMessage("remove", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Reserved by another", "The item is currently reserved by another. Try again later."));
        }
    }

    /**
     * Proxy to {@link CartBean#remove(Item)}
     *
     * @param item The item to remove.
     */
    public void remove(Item item)
    {
        if (!cart.remove(item))
        {
            FacesContext.getCurrentInstance().addMessage("remove",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not remove item", "Failed to remove item"));
        }
    }
    
    /**
     * Proxy to {@link CartBean#purchase()}
     */
    public void purchase()
    {
        try
        {
            if (cart.purchase())
            {
                FacesContext.getCurrentInstance().addMessage("purchase", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Purchase successful", "All items purchased successfully."));
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage("purchase", new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "No items in cart", "The cart contains no items."));
            }
        }
        catch (IOException e)
        {
            FacesContext.getCurrentInstance().addMessage("purchase",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Purchase failed", e.getLocalizedMessage()));
        }
    }
}
