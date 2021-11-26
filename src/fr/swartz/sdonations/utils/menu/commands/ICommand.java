package fr.swartz.sdonations.utils.menu.commands;

public abstract class ICommand {
	
  public ICommand() {}
  public abstract void onCommand(CommandArgs paramCommandArgs);
}
