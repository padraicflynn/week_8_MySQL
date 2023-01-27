package projects;

import java.math.BigDecimal;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;
 

public class ProjectsApp {
	
	private ProjectService projectService = new ProjectService();
	

	//Scanner: this is an object that users can input a number into to tell the menu what to do
private Scanner scanner = new Scanner(System.in);
	
	
	// @formatter:off
	
 //making a current project variable that can become what holds a current project using option 3
private Project curProject;

	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project"
			);
			
			// @formatter:on
	
	//main method: this will run the menu
	public static void main(String[] args) {
		  new ProjectsApp().processUserSelections();
		  
	}

	
	//this method has all the menu options
			private void processUserSelections() {
				//local variable, done is false, meaning the menu will run until we say it's done
				boolean done = false;
				
				//while not done, meaning the menu will do this until it done = true
				while(!done) {
					
					//using a try/catch block to attempt to get a user input, if it fails we will say try again
					try {
						//getUserSelection is the method we will make below that will accept input via system.in (i think)
						int selection = getUserSelection();
						
						switch(selection) { 
						case -1:
							done = exitMenu();
							break;
							
						case 1:
							createProject();
							break;
							
						case 2:
							listProjects();
							break;
							
						case 3: 
							selectProject();
							break;
							
							
							default:
								System.out.println("\n" + selection + " is not a valid selection. Try again.");
								break;
								
							
						}
					}
					
					//catching exception so if the user puts an invalid choice it will try again instead of crashing
					// \n adds a space to make things easy to read, "error" plus the error then "try again"
					catch(Exception e) {
						System.out.println("\nError: " + e + "Try again.");
					}
				}
				
			}

			
			
			
			
			private void selectProject() {
				 listProjects();
				 Integer projectId = getIntInput("Enter a project ID to select a project");
				 curProject = null;
				 curProject = projectService.fetchProjectById(projectId);
				
			}


			private void listProjects() {
		 List<Project> projects = projectService.fetchAllProjects();
				 
				 System.out.println("\nProjects:");
		 
projects.forEach(project -> System.out.println("   " + project.getProjectId() + ":  " + project.getProjectName()));
		
	}


			private void createProject() {
				
				//setting what each object will be and prompt the user for
				
				String projectName = getStringInput("Enter the project name");
				BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours: ");
				BigDecimal actualHours = getDecimalInput("Enter the actual hours: ");
				Integer difficulty = getIntInput("Enter the project difficulty on a scale of 1-5: ");
				String notes = getStringInput("Enter the project notes");
				
				//making an object called project 
				Project project = new Project();
				
				//setting what these objects will contain/display 
				project.setActualHours(actualHours);
				project.setDifficulty(difficulty);
				project.setEstimatedHours(estimatedHours);
				project.setProjectName(projectName);
				project.setNotes(notes);
				
				
				Project dbProject = projectService.addProject(project);
				System.out.println("You have successfully created project: " + dbProject);
				
	}





			private BigDecimal getDecimalInput(String prompt) {
				 String input = getStringInput(prompt);
				 
				 if (Objects.isNull(input)) { 
					 return null;
				 }
				 try {
					 return new BigDecimal(input).setScale(2);
				 }
				 catch(NumberFormatException e) {
					 throw new DbException(input + " is not a valid decimal number.");
				 }
			}





			private boolean exitMenu() {
				 System.out.println("Exiting the menu now.");
				 return true;
			}





	// here the method that will take the user input
	private int getUserSelection() {
printOperations();
Integer input = getIntInput("Enter a menu selection");

//when using the menu if you just hit enter that means the input is null, which will
// translate to -1, we will make it so an input of nothing/null leads to an input 
//of -1, which we will say -1 means exit the menu

return Objects.isNull(input) ? -1 : input;
		 
	}

	
	
	
	
	//method for converting the user input into something usable 
			private Integer getIntInput(String prompt) {
				String input = getStringInput(prompt);
				//if the object input is null then return null which will be -1 leading to exit menu
				if(Objects.isNull(input)) {
					return null;	
				}
				// try to return the value of input, if not throw an exception
				 try { 
					 return Integer.valueOf(input);
				 }
				 catch(NumberFormatException e) {
					 throw new DbException(input + "is not a valid number.");
				 }
			}
			
			
			
			
			
//this is the method that gets the user input, or prompt, as a string
			private String getStringInput(String prompt) {
				System.out.print(prompt + ": ");
				String input = scanner.nextLine();
				
				return input.isBlank() ? null : input.trim();
		 
	}




			
			

			private void printOperations() {
				 System.out.println("\nThese are the available selections. Press the Enter key to quit:");

				 
				 
				for (String line : operations) {
					System.out.println("  " + line);
				 }
				 
				if(Objects.isNull(curProject)) {
					System.out.println("\nYou are not working with a project.");
				}
				else {
					System.out.println("\nYou are working with project: " + curProject);
				}
			}

}
