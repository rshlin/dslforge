define('ace/ext/tooltip', ['require', 'exports', 'module' , 'ace/lib/dom', 'ace/lib/event', 'ace/range'], function(require, exports, module) {
	
  var dom = require("ace/lib/dom");
  var event = require("ace/lib/event");
  var Range = require("ace/range").Range;

  var tooltipNode;

  var DocumentationTooltip = function(editor) {
      if (editor.DocumentationTooltip)
          return;
      editor.DocumentationTooltip = this;    
      this.editor = editor;
     
      editor.tooltip = tooltipNode || this.$init();

      this.update = this.update.bind(this);
      this.onMouseMove = this.onMouseMove.bind(this);
      this.onMouseOut = this.onMouseOut.bind(this);
      event.addListener(editor.renderer.scroller, "mousemove", this.onMouseMove);
      event.addListener(editor.renderer.content, "mouseout", this.onMouseOut);
  };

  (function(){
      this.token = {};
      this.range = new Range();
      
      this.update = function() {
          this.$timer = null;
          
          var r = this.editor.renderer;
          if (this.lastT - (r.timeStamp || 0) > 1000) {
              r.rect = null;
              r.timeStamp = this.lastT;
              this.maxHeight = window.innerHeight;
              this.maxWidth = window.innerWidth;
          }

          var canvasPos = r.rect || (r.rect = r.scroller.getBoundingClientRect());
          var offset = (this.x + r.scrollLeft - canvasPos.left - r.$padding) / r.characterWidth;
          var row = Math.floor((this.y + r.scrollTop - canvasPos.top) / r.lineHeight);
          var col = Math.round(offset);

          var screenPos = {row: row, column: col, side: offset - col > 0 ? 1 : -1};
          var session = this.editor.session;
          var docPos = session.screenToDocumentPosition(screenPos.row, screenPos.column);
          var token = session.getTokenAt(docPos.row, docPos.column);

          if (!token && !session.getLine(docPos.row)) {
              token = {
                  type: "",
                  value: "",
                  state: session.bgTokenizer.getState(0)
              };
          }
          if (!token) {
              session.removeMarker(this.marker);
              tooltipNode.style.display = "none";
              this.isOpen = false;
              return;
          }
          if (!this.isOpen) {
              tooltipNode.style.display = "";
              this.isOpen = true;
          }
          
          var self = this;
          var tokenText = null;
         
          tokenText = '';
          tokenText += 'Hello!';
          
          if (!tokenText) {
            session.removeMarker(this.marker);
            tooltipNode.style.display = "none";
            this.isOpen = false;
            return;
        }
          
          if (self.tokenText != tokenText) {
              tooltipNode.textContent = tokenText;
              self.tooltipWidth = tooltipNode.offsetWidth;
              self.tooltipHeight = tooltipNode.offsetHeight;
              self.tokenText = tokenText;
          }
          
          self.updateTooltipPosition(self.x, self.y);

          self.token = token;
          session.removeMarker(self.marker);
          self.range = new Range(docPos.row, token.start, docPos.row, token.start + token.value.length);
          self.marker = session.addMarker(self.range, "ace_bracket", "text");
      };
      
      this.onMouseMove = function(e) {
          this.x = e.clientX;
          this.y = e.clientY;
          if (this.isOpen) {
              this.lastT = e.timeStamp;
              this.updateTooltipPosition(this.x, this.y);
          }
          if (!this.$timer)
              this.$timer = setTimeout(this.update, 100);
      };
      
      this.onMouseOut = function(e) {
          var t = e && e.relatedTarget;
          var ct = e &&  e.currentTarget;
          while(t && (t = t.parentNode)) {
              if (t == ct)
                  return;
          }
          tooltipNode.style.display = "none";
          this.editor.session.removeMarker(this.marker);
          this.$timer = clearTimeout(this.$timer);
          this.isOpen = false;
      };
      
      this.updateTooltipPosition = function(x, y) {
          var st = tooltipNode.style;
          if (x + 10 + this.tooltipWidth > this.maxWidth)
              x = window.innerWidth - this.tooltipWidth - 10;
          if (y > window.innerHeight * 0.75 || y + 20 + this.tooltipHeight > this.maxHeight)
              y = y - this.tooltipHeight - 30;
          
          st.left = x + 10 + "px";
          st.top = y + 20 + "px";
      };

      this.$init = function() {
          tooltipNode = document.documentElement.appendChild(dom.createElement("div"));
          var st = tooltipNode.style;
          st.position = "fixed";
          st.display = "none";
          st.background = "lightyellow";
          st.borderRadius = "";
          st.border = "1px solid gray";
          st.padding = "1px";
          st.zIndex = 1000;
          st.fontFamily = "monospace";
          st.whiteSpace = "pre-line";
          return tooltipNode;
      };

      this.destroy = function() {
          this.onMouseOut();
          event.removeListener(this.editor.renderer.scroller, "mousemove", this.onMouseMove);
          event.removeListener(this.editor.renderer.content, "mouseout", this.onMouseOut);
          delete this.editor.DocumentationTooltip;    
      };

  }).call(DocumentationTooltip.prototype);

  exports.DocumentationTooltip = DocumentationTooltip;

  });
