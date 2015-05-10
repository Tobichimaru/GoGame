# GoGame
Coursework

Development of client-server applications for the game of GO USING JAVA

Game Go - one of the most interesting and challenging board logic games. 
Despite its prevalence, there is not such a large number of applications for the game of Go.
At the moment, the client-server applications have become ubiquitous, 
so being able to play on the network is simply necessary for a gaming application.
My task was to write a client-server application for playing Go and study of issues 
related to the theory of Go and subtleties of the rules.

This aplication implements:
1) Graphic interface including:
• Display boards and stones
• Processing of mouse clicks
• Create menus and Help

2) The logic of the game:
• The problem of the death group
• Scoring
• Implementation of methods Undo (step back) and Redo (Step Forward)
• Saving and loading games via serialization file
• Implementation of the rule Ko

3) Game on the network:
• Create a multithreaded server
• Creating a client
• Methods of interaction between client and server
