package com.example.hars;

import com.example.hars.Models.User;

public interface Subject {
    public void registerObserver(Observer ob);
    public void deleteObserver(Observer ob);
    public void notifyTheStatus(User.Status_user status);
}
