---


---

<h1 id="introduction">Introduction</h1>
<p>This trading application is an online stock trading simulation REST API which can be used to create an account which would allow account holder to buy and sell stocks from Investor Exchange i.e IEX. Traders can withdraw money and/or deposit money into their account. They can also view latest quotes of any stock directly from this application.</p>
<p>This REST API can be used by front-end developers, mobile-app developers, and traders.</p>
<p>The architecture used here is based on microservices concept which is implemented using SpringBoot, IEX API and PSQL database.</p>
<h1 id="quick-start">Quick Start</h1>
<p>CentOS 7, Docker and Java are to be installed prior to use this REST API.</p>
<h2 id="initial-setup">Initial Setup</h2>
<p>Create an account on <a href="https://iexcloud.io/">https://iexcloud.io</a>  and get your iex_public_token</p>
<pre><code>#put the following env var in ~/.bash_profile
export IEX_PUB_TOKEN='your_iex_pub_key'
export PSQL_PASSWORD="your password" 
export PSQL_USER=" psql user" 
export PSQL_HOST="psql host"
$ source ~/.bash_profile
</code></pre>
<h2 id="usage">Usage</h2>
<ol>
<li>Start docker<br>
<code>sudo systemctl start docker</code></li>
<li>Download the source code.<br>
<code>https://github.com/saud-aslam/trading-app.git</code></li>
<li>Build trading app<br>
<code>sudo docker build -t trading-app .</code></li>
<li>Build psql image<br>
<code>cd psql/</code><br>
<code>sudo docker build -t jrvs-psql .</code></li>
<li>Initialize PostgreSQL and start SpringBoot application via <code>bash start_up.sh PSQL_HOST PSQL_USER PSQL_PASSWORD</code></li>
<li>Consume the API through your browser by: <code>http://localhost:8080/swagger-ui.html</code></li>
</ol>
<h2 id="rest-api-usage">REST API usage</h2>
<h3 id="swagger">Swagger</h3>
<p>Swagger is an open-source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful web services. By visualizing the endpoints of the program, it allows user to access function and execute them with the help of interactive GUI.</p>
<h3 id="qoute-controller">Qoute Controller</h3>
<p>Through this endpoint, IEX quotes are accessed through sending HTTP requests to the IEXCloud.</p>
<ul>
<li><code>GET '/quote/dailyList'</code>  - List all quotes securities already in the database.</li>
<li><code>GET '/quote/iex/ticker/{ticker}</code> : Input IEX symbol and shows IEX market data’s quotes information.</li>
<li><code>POST '/quote/ticker/{ticker}</code>  - Adds a new ticker to the Quote database</li>
<li><code>PUT '/quote/</code>  - Allows user to manually insert quote data.</li>
<li><code>PUT '/quote/iexMarketData'</code>  - Updates all quotes which are in database.</li>
</ul>
<h3 id="trader-controller">Trader Controller</h3>
<p>Provides trader to add or withdraw amount from his account. New account can be created or old one can be deleted.</p>
<ul>
<li><code>DELETE '/trader/traderId/{traderId}'</code>  - Delete a trader with trader id provided that the trader has no account balance and no security in his account.</li>
<li><code>POST '/trader/</code>  - Creates a trader with specified given details</li>
<li><code>PUT '/trader/deposit/traderId/{traderId}/amount/{amount}</code>  - Adds amount to trader’s account balance</li>
<li><code>PUT '/trader/withdraw/traderId/{traderId}/amount/{amount}</code>  - Deduct amount from trader’s account balance</li>
</ul>
<h3 id="order-controller">Order Controller</h3>
<p>A trader can buy or sell a stock by using this endpoint.</p>
<ul>
<li><code>POST '/order/marketOrder'</code>  - Based on the input size, buying or selling of stock takes place. It the size is positive and account have enough money, then buying will get executed and if the size is negative and account has enough securities, then selling will occur.</li>
</ul>
<h3 id="dashboard-controller">Dashboard Controller</h3>
<p>Shows trader his account and securties details.</p>
<ul>
<li><code>GET '/dashboard/portfolio/traderId/{traderId}'</code>  -Gives the quotes and position of all the securities which the trader have.</li>
<li><code>GET '/dashboard/profile/traderId/{traderId}'</code>  - Shows the Trader and Account data from the database of the given trader_id.</li>
</ul>
<h3 id="app-controller">App Controller</h3>
<ul>
<li><code>GET '/health'</code>  to see whether the SprinBoot app is running.</li>
</ul>
<h2 id="architecture">Architecture</h2>
<ul>
<li>
<p>Draw a component diagram which contains controller, service, DAO, storage layers (you can mimic the diagram from the guide)</p>
</li>
<li>
<p><strong>Controller</strong>  - This layer is what the user interact with. Together with Swagger UI, the function of this layer is to invoke the service layer (in most cases) based on the input from the user. The request is translated and the response is retrieved back to this layer where it is shown to user in JSON format.</p>
</li>
<li>
<p><strong>Service</strong>  - This layer performs business logic and interacts with DAO layer. For example, validity of incoming Trader account details would be performed here before it being sent to TraderDao to be saved in the database.</p>
</li>
<li>
<p><strong>DAO</strong>  - This layer performs the create, read, update, and delete actions on the database.</p>
</li>
<li>
<p><strong>SpringBoot</strong>:</p>
</li>
<li>
<p><strong>PSQL Database</strong>  - This is the database used to store our data in the tables and views which would be persisted here. The data can always be retrieved back whenever we want to.</p>
</li>
<li>
<p><strong>IEX</strong>  - IEX provides us REST API  which we used to obtain stock information. By sending HTTP request to it, we get a response, which is parsed accrding to the needs.</p>
</li>
</ul>
<h2 id="improvements">Improvements</h2>
<ul>
<li>We have assumed that trader and account has one-one to relation. This can be improved by allowing trader to have multiple accounts.</li>
<li>Auto-increament ids once deleted can not be re-used. In some cases, it can be re-used in future implementation.</li>
<li>Auto-updation of dailylist is possible and can be implemented in future.</li>
<li>Email/sms alert can be set e.g when the price of specific security is changed.</li>
<li>Once the market is closed, a trader can not trade in stocks. This can be improved by saving the quote  just before the market closes and used that to allow trader to trade and immediately execute their request when the market re-opens.</li>
</ul>

